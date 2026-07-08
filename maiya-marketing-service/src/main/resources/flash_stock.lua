-- flash_stock.lua
-- Redis 原子扣库存 + 限购校验
-- KEYS[1] = flash:stock:{fsId}     库存 key
-- KEYS[2] = flash:users:{fsId}     已购买用户 set
-- ARGV[1] = uid                    用户ID
-- ARGV[2] = limit                  限购数量

-- 1. 检查活动是否有效
local active = redis.call('EXISTS', 'flash:active:' .. KEYS[1])
if active == 0 then
    return -3  -- 活动不存在或已结束
end

-- 2. 检查用户已购数量
local userBuyCount = redis.call('GET', 'flash:buy:' .. KEYS[1] .. ':' .. ARGV[1])
if userBuyCount and tonumber(userBuyCount) >= tonumber(ARGV[2]) then
    return -4  -- 超出限购数量
end

-- 3. 扣减库存
local stock = redis.call('DECR', KEYS[1])
if stock < 0 then
    redis.call('INCR', KEYS[1])  -- 回滚
    return -1  -- 库存不足
end

-- 4. 增加用户购买计数
redis.call('INCR', 'flash:buy:' .. KEYS[1] .. ':' .. ARGV[1])
redis.call('EXPIRE', 'flash:buy:' .. KEYS[1] .. ':' .. ARGV[1], 86400)

-- 5. 加入已购买集合
redis.call('SADD', KEYS[2], ARGV[1])
redis.call('EXPIRE', KEYS[2], 86400)

-- 6. 延长库存 key 存活
redis.call('EXPIRE', KEYS[1], 86400)

return stock  -- 返回剩余库存（>=0 表示成功）
