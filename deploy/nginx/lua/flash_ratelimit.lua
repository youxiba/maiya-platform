-- flash_ratelimit.lua
-- 基于共享内存的动态限流模块
-- 支持运行时调整限流参数，无需 reload nginx
--
-- 部署位置：/etc/nginx/lua/flash_ratelimit.lua
-- 依赖：lua_shared_dict flash_local_cnt 100m
--       lua_shared_dict flash_config    10m

local ngx       = ngx
local shared    = ngx.shared
local time      = ngx.time
local log       = ngx.log
local WARN      = ngx.WARN
local ERR       = ngx.ERR
local DEBUG     = ngx.DEBUG

-- ======== 默认配置（可从 flash_config 覆盖） ========
local DEFAULTS = {
    user_qps       = 1000,    -- 每用户 QPS
    user_burst     = 200,     -- 用户突发
    ip_qps         = 3,       -- 每 IP QPS
    ip_burst       = 5,       -- IP 突发
    total_qps      = 5000,    -- 总入口 QPS
    total_burst    = 1000,    -- 总突发
    downgrade_rate = 1.0,     -- 降级系数 (0.0~1.0)
    enable_vip     = true,    -- VIP 用户免限流
}

local _M = {}

-- ======== 从共享内存获取配置 ========
function _M.get_config(key)
    local val = shared.flash_config:get("config:" .. key)
    if val then
        return val
    end
    return DEFAULTS[key]
end

function _M.get_number(key)
    local val = _M.get_config(key)
    if type(val) == "number" then
        return val
    end
    return tonumber(val) or DEFAULTS[key]
end

-- ======== VIP 用户判断 ========
function _M.is_vip(uid)
    if not uid or uid == "" then
        return false
    end
    local vips_json = shared.flash_config:get("vip_list")
    if vips_json then
        local cjson = require("cjson")
        local ok, vip_data = pcall(cjson.decode, vips_json)
        if ok and type(vip_data) == "table" and vip_data[uid] then
            return true
        end
    end
    return false
end

-- ======== IP 级本地限流（滑动窗口） ========
-- 基于 lua_shared_dict 实现跨 worker 计数
-- 每个 IP 每秒一个计数器，TTL 2 秒自动过期
function _M.check_ip_limit(ip)
    if not ip or ip == "" then
        return true
    end

    local ip_qps = _M.get_number("ip_qps")
    local ip_burst = _M.get_number("ip_burst")
    local downgrade = _M.get_number("downgrade_rate")

    local effective_qps = math.max(1, math.floor(ip_qps * downgrade))
    local window_key = "ip:" .. ip .. ":" .. math.floor(time() / 1)

    local count, err = shared.flash_local_cnt:incr(window_key, 1, 0, 2)
    if not count then
        log(ERR, "共享内存 incr 失败: ", err)
        return true  -- 共享内存异常时放行
    end

    if count > effective_qps + ip_burst then
        log(WARN, "IP限流: ip=", ip, " count=", count, " limit=", effective_qps + ip_burst)
        return false
    end

    return true
end

-- ======== 用户级限流（滑动窗口） ========
-- VIP 用户免限流
function _M.check_user_limit(uid)
    if not uid or uid == "" then
        return true
    end

    -- VIP 用户免限流
    if _M.get_number("enable_vip") > 0 and _M.is_vip(uid) then
        return true
    end

    local user_qps = _M.get_number("user_qps")
    local user_burst = _M.get_number("user_burst")
    local downgrade = _M.get_number("downgrade_rate")

    local effective_qps = math.max(1, math.floor(user_qps * downgrade))
    local window_key = "user:" .. uid .. ":" .. math.floor(time() / 1)

    local count = shared.flash_local_cnt:incr(window_key, 1, 0, 2)
    if not count then
        return true
    end

    if count > effective_qps + user_burst then
        log(DEBUG, "用户限流: uid=", uid, " count=", count)
        return false
    end

    return true
end

-- ======== 总入口限流 ========
function _M.check_total_limit()
    local total_qps = _M.get_number("total_qps")
    local total_burst = _M.get_number("total_burst")
    local downgrade = _M.get_number("downgrade_rate")

    local effective_qps = math.max(1, math.floor(total_qps * downgrade))
    local window_key = "total:" .. math.floor(time() / 1)

    local count = shared.flash_local_cnt:incr(window_key, 1, 0, 2)
    if not count then
        return true
    end

    if count > effective_qps + total_burst then
        log(WARN, "总入口限流: count=", count, " limit=", effective_qps + total_burst)
        return false
    end

    return true
end

-- ======== 三重检查（合并调用） ========
-- IP → 用户 → 总入口 逐级检查
-- 任意一级失败则限流
function _M.check_all(ip, uid)
    -- 1. IP 级检查
    if not _M.check_ip_limit(ip) then
        return false, "IP_REQUEST_LIMIT"
    end

    -- 2. 用户级检查
    if not _M.check_user_limit(uid) then
        return false, "USER_REQUEST_LIMIT"
    end

    -- 3. 总入口检查
    if not _M.check_total_limit() then
        return false, "TOTAL_REQUEST_LIMIT"
    end

    return true, "OK"
end

-- ======== 实时调整限流参数 ========
-- 从管理 API 或 Redis 同步配置，写入共享内存
-- 写入后 worker 即时生效，无需 reload
function _M.update_config(key, value)
    local ok, err = shared.flash_config:set("config:" .. key, value)
    if ok then
        log(WARN, "限流配置更新: ", key, "=", tostring(value))
    else
        log(ERR, "限流配置更新失败: key=", key, " err=", err)
    end
    return ok
end

-- ======== 从 Redis 同步配置 ========
-- 由 init_worker 定时器每 5 秒调用
function _M.sync_from_redis(redis_host, redis_port)
    local redis = require("resty.redis")
    local red = redis:new()
    red:set_timeout(100)

    local ok, err = red:connect(redis_host, redis_port)
    if not ok then
        log(ERR, "连接Redis失败: ", err)
        return
    end

    -- 读取动态限流配置
    local keys, err = red:keys("nginx:ratelimit:*")
    if keys and #keys > 0 then
        for _, key in ipairs(keys) do
            local val, _ = red:get(key)
            if val then
                local config_name = key:match("nginx:ratelimit:(.*)")
                if config_name then
                    _M.update_config(config_name, val)
                end
            end
        end
    end

    -- 读取 VIP 名单
    local vip_data, _ = red:get("nginx:vip_list")
    if vip_data then
        shared.flash_config:set("vip_list", vip_data)
    end

    red:close()
end

return _M
