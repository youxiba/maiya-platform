-- auto_downgrade.lua
-- 自动降级：根据后端响应时间和错误率动态调整限流阈值
-- 写入 flash_config 共享内存，即时生效
--
-- 部署位置：/etc/nginx/lua/auto_downgrade.lua

local ngx = ngx
local shared = ngx.shared
local time = ngx.time
local log = ngx.log
local WARN = ngx.WARN
local ERR = ngx.ERR

local _M = {}

-- 滑动窗口统计数据（内存表，每个 worker 独立）
local stats_window = {}
local WINDOW_SIZE = 10   -- 统计最近 10 秒
local CLEANUP_INTERVAL = 60  -- 每 60 秒清理一次旧数据

-- ======== 记录后端响应 ========
-- 由 body_filter 阶段调用
function _M.record_upstream(response_time_ms, status)
    local now = time()
    local second = math.floor(now)

    if not stats_window[second] then
        stats_window[second] = {
            total  = 0,
            slow   = 0,   -- >1000ms
            error  = 0,   -- >=500
            sum_ms = 0
        }
    end

    local stat = stats_window[second]
    stat.total  = stat.total + 1
    stat.sum_ms = stat.sum_ms + response_time_ms

    if response_time_ms > 1000 then
        stat.slow = stat.slow + 1
    end
    if status and status >= 500 then
        stat.error = stat.error + 1
    end

    -- 定期清理旧数据
    if stat.total % 100 == 0 then
        for s in pairs(stats_window) do
            if now - s > CLEANUP_INTERVAL then
                stats_window[s] = nil
            end
        end
    end
end

-- ======== 计算降级系数 ========
-- 根据最近 WINDOW_SIZE 秒的统计计算
-- 返回: 0.1 ~ 1.0
function _M.calculate_downgrade_rate()
    local now = time()
    local total, slow, err = 0, 0, 0
    local sum_ms = 0

    for s, stat in pairs(stats_window) do
        if now - s <= WINDOW_SIZE then
            total  = total  + stat.total
            slow   = slow   + stat.slow
            err    = err    + stat.error
            sum_ms = sum_ms + stat.sum_ms
        end
    end

    if total == 0 then
        return 1.0  -- 无数据，不降级
    end

    local slow_rate = slow / total
    local err_rate  = err  / total

    -- 加权评分：慢请求 40% + 错误率 60%
    local weighted = slow_rate * 0.4 + err_rate * 0.6

    -- 降级系数 = 1.0 - 加权评分，范围 [0.1, 1.0]
    local rate = math.max(0.1, math.min(1.0, 1.0 - weighted))

    if rate < 1.0 then
        log(WARN, "自适应降级: rate=",
            string.format("%.2f", rate),
            " slow=", string.format("%.1f%%", slow_rate * 100),
            " err=",  string.format("%.1f%%", err_rate * 100),
            " avg_ms=", math.floor(sum_ms / total))
    end

    return rate
end

-- ======== 定时更新降级系数 ========
-- 由 init_worker 定时器每 3 秒调用
function _M.start_auto_downgrade()
    local flash_ratelimit = require("flash_ratelimit")
    local ngx_timer = ngx.timer

    local function adjust(premature)
        if premature then
            return
        end
        local rate = _M.calculate_downgrade_rate()
        flash_ratelimit.update_config("downgrade_rate", rate)
        ngx_timer.every(3, adjust)
    end

    ngx_timer.every(3, adjust)
end

return _M
