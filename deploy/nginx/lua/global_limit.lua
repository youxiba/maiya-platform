-- global_limit.lua
-- 全局限流：跨 Nginx 节点共享计数器
-- 基于 Redis 滑动窗口（精确到秒）
-- 只在本地限流放行后才检查，减少 Redis 压力
--
-- 部署位置：/etc/nginx/lua/global_limit.lua

local redis = require("resty.redis")
local ngx_log = ngx.log
local ngx_WARN = ngx.WARN
local ngx_ERR = ngx.ERR

local REDIS_HOST = "hadoop003"
local REDIS_PORT = 6379
local REDIS_TIMEOUT = 50  -- 50ms 超时，不影响主流程

local _M = {}

-- ======== 获取 Redis 连接（连接池复用） ========
local function get_redis()
    local red = redis:new()
    red:set_timeout(REDIS_TIMEOUT)
    local ok, err = red:connect(REDIS_HOST, REDIS_PORT)
    if not ok then
        ngx_log(ngx_ERR, "GlobalLimit Redis 连接失败: ", err)
        return nil
    end
    red:set_keepalive(30000, 100)  -- 30 秒空闲，最多 100 个连接
    return red
end

-- ======== 全局 IP 限流检查 ========
-- Redis Key: flash:global:ip:{ip}:{秒级时间戳}
-- 仅在本地限流通过后调用
function _M.check_global_ip(ip)
    if not ip or ip == "" then
        return true
    end

    local red = get_redis()
    if not red then
        return true  -- Redis 不可用则放行
    end

    local key = "flash:global:ip:" .. ip .. ":" .. math.floor(ngx.time() / 1)
    local count, err = red:incr(key)
    if count then
        red:expire(key, 5)
        if count > 10 then  -- 全局每个 IP 每秒最多 10 次
            ngx_log(ngx_WARN, "全局IP限流: ip=", ip, " count=", count)
            return false
        end
    end
    return true
end

-- ======== 全局 SKU 限流检查 ========
-- Redis Key: flash:global:sku:{fsId}:{秒级时间戳}
function _M.check_global_sku(fsId)
    if not fsId or fsId == "" then
        return true
    end

    local red = get_redis()
    if not red then
        return true
    end

    local key = "flash:global:sku:" .. fsId .. ":" .. math.floor(ngx.time() / 1)
    local count, err = red:incr(key)
    if count then
        red:expire(key, 5)
        if count > 10000 then  -- 全局每个 SKU 每秒最多 10000
            ngx_log(ngx_WARN, "全局SKU限流: fsId=", fsId, " count=", count)
            return false
        end
    end
    return true
end

return _M
