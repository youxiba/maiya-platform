-- flash_stock_v2.lua
-- Redis 原子扣库存 + 限购校验（优化版）
--
-- KEYS[1] = flash:active:{fsId}       活动状态 key
-- KEYS[2] = flash:stock:{fsId}        库存 key
-- KEYS[3] = flash:buy:{fsId}:{uid}    用户已购数量 key
-- KEYS[4] = flash:users:{fsId}        已购买用户 set
--
-- ARGV[1] = uid                       用户 ID
-- ARGV[2] = limitQty                  限购数量
-- ARGV[3] = ttl                       Redis key 过期时间（秒）
--
-- 返回值：
--   >=0  → 剩余库存（扣减成功）
--   -1   → 库存不足
--   -2   → 活动不存在或已结束
--   -3   → 超出限购数量
--   -4   → 重复购买

-- 1. 活动状态校验（修复：独立 KEYS[1]，不再错误拼接）
local active = redis.call('EXISTS', KEYS[1])
if active == 0 then
    return -2
end

-- 2. 用户去重（防止同一用户并发重复扣库存）
local isMember = redis.call('SISMEMBER', KEYS[4], ARGV[1])
if isMember == 1 then
    return -4
end

-- 3. 限购校验
local buyCount = redis.call('GET', KEYS[3])
if buyCount and tonumber(buyCount) >= tonumber(ARGV[2]) then
    return -3
end

-- 4. 原子扣库存（先 GET 判断 → DECR，避免负数回滚竞态）
local stock = redis.call('GET', KEYS[2])
if not stock or tonumber(stock) <= 0 then
    return -1
end

redis.call('DECR', KEYS[2])

-- 5. 记录用户购买
redis.call('INCR', KEYS[3])
redis.call('EXPIRE', KEYS[3], 86400)

redis.call('SADD', KEYS[4], ARGV[1])
redis.call('EXPIRE', KEYS[4], tonumber(ARGV[3]))

-- 6. 返回剩余库存
local remain = redis.call('GET', KEYS[2])
return tonumber(remain)
