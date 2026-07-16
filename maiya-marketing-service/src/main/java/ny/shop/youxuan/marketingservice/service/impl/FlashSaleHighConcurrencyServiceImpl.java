package ny.shop.youxuan.marketingservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.PostConstruct;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ny.shop.youxuan.common.constant.MqTopic;
import ny.shop.youxuan.marketingservice.dto.FlashOrderMessage;
import ny.shop.youxuan.marketingservice.entity.FlashSale;
import ny.shop.youxuan.marketingservice.mapper.FlashSaleMapper;
import ny.shop.youxuan.marketingservice.service.FlashSaleHighConcurrencyService;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 秒杀高并发实现（优化版）
 *
 * 核心流程：预热→Redis Lua 原子扣库存→同步发送 MQ→超时回滚
 *
 * 优化点：
 * 1. 修复 Lua 脚本活动校验 Key 拼接 Bug
 * 2. 请求幂等（requestId 防重）
 * 3. 同步发送 MQ（替换 async fire-and-forget）
 * 4. 预热从 DB 读取库存配置
 * 5. 失败补偿队列
 */
@Service
public class FlashSaleHighConcurrencyServiceImpl implements FlashSaleHighConcurrencyService {

    private static final Logger log = LoggerFactory.getLogger(FlashSaleHighConcurrencyServiceImpl.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private FlashSaleMapper flashSaleMapper;

    @Autowired(required = false)
    private RocketMQTemplate rocketMQTemplate;

    private DefaultRedisScript<Long> stockLuaScript;

    private static final String KEY_ACTIVE = "flash:active:";
    private static final String KEY_STOCK  = "flash:stock:";
    private static final String KEY_BUY    = "flash:buy:";
    private static final String KEY_USERS  = "flash:users:";
    private static final String KEY_REQ    = "flash:request:";

    @PostConstruct
    public void init() {
        stockLuaScript = new DefaultRedisScript<>();
        stockLuaScript.setLocation(new ClassPathResource("flash_stock_v2.lua"));
        stockLuaScript.setResultType(Long.class);
    }

    /**
     * 库存预热（从 DB 读取配置）
     */
    @Override
    public boolean preheatStock(String fsId) {
        FlashSale sale = flashSaleMapper.findByFsId(fsId);
        if (sale == null || !sale.getEnable()) {
            log.warn("秒杀活动不可用: fsId={}", fsId);
            return false;
        }

        long ttl = (sale.getEndTime() - System.currentTimeMillis()) / 1000;
        if (ttl <= 0) {
            log.warn("活动已结束: fsId={}", fsId);
            return false;
        }

        // 使用 Redis 事务确保预热原子性
        redisTemplate.execute((org.springframework.data.redis.core.RedisCallback<Object>) connection -> {
            connection.multi();
            connection.stringCommands().set((KEY_ACTIVE + fsId).getBytes(),
                    "1".getBytes(),
                    org.springframework.data.redis.core.types.Expiration.seconds(ttl),
                    org.springframework.data.redis.connection.RedisStringCommands.SetOption.UPSERT);
            connection.stringCommands().set((KEY_STOCK + fsId).getBytes(),
                    String.valueOf(sale.getTotalStock()).getBytes(),
                    org.springframework.data.redis.core.types.Expiration.seconds(ttl),
                    org.springframework.data.redis.connection.RedisStringCommands.SetOption.UPSERT);
            // 清理预热前的残留数据
            connection.keyCommands().del((KEY_USERS + fsId).getBytes());
            connection.exec();
            return null;
        });

        log.info("库存预热完成: fsId={}, stock={}, ttl={}s", fsId, sale.getTotalStock(), ttl);
        return true;
    }

    /**
     * 秒杀抢购（优化版）
     *
     * @param fsId      秒杀活动 ID
     * @param uid       用户 ID
     * @param requestId 请求幂等 ID
     * @return 抢购结果
     */
    @Override
    public JSONObject grabFlashSale(String fsId, String uid, String requestId) {
        JSONObject result = new JSONObject();
        long startTime = System.currentTimeMillis();

        // ---- 1. 请求幂等（防止 MQ 重试导致重复扣库存） ----
        String reqKey = KEY_REQ + requestId;
        Boolean firstSeen = redisTemplate.opsForValue().setIfAbsent(
                reqKey, "1", Duration.ofSeconds(30));
        if (Boolean.FALSE.equals(firstSeen)) {
            // 相同 requestId 已处理过
            result.put("code", 0);
            result.put("msg", "请求已处理");
            return result;
        }

        // ---- 2. 从 DB 获取活动配置 ----
        FlashSale sale = flashSaleMapper.findByFsId(fsId);
        if (sale == null || !sale.getEnable()) {
            result.put("code", -3);
            result.put("msg", "活动不存在或已关闭");
            return result;
        }

        // ---- 3. 执行 Lua 原子扣库存 ----
        long ttl = Math.max(60,
                (sale.getEndTime() - System.currentTimeMillis()) / 1000);

        List<String> keys = Arrays.asList(
                KEY_ACTIVE + fsId,     // KEYS[1]
                KEY_STOCK  + fsId,     // KEYS[2]
                KEY_BUY    + fsId + ":" + uid,  // KEYS[3]
                KEY_USERS  + fsId      // KEYS[4]
        );

        Long stockRemain = redisTemplate.execute(
                stockLuaScript, keys,
                uid,
                String.valueOf(sale.getLimitQty() != null ? sale.getLimitQty() : 1),
                String.valueOf(ttl)
        );

        if (stockRemain == null) {
            result.put("code", -99);
            result.put("msg", "系统繁忙");
            return result;
        }

        // ---- 4. 失败场景 ----
        if (stockRemain < 0) {
            String msg;
            switch (stockRemain.intValue()) {
                case -1: msg = "库存不足";            break;
                case -2: msg = "活动未开始或已结束";    break;
                case -3: msg = "超出限购数量";         break;
                case -4: msg = "您已抢购过该商品";      break;
                default: msg = "抢购失败";
            }
            result.put("code", stockRemain.intValue());
            result.put("msg", msg);
            return result;
        }

        // ---- 5. 成功 → 同步发送 MQ ----
        FlashOrderMessage orderMsg = FlashOrderMessage.create(fsId, uid, requestId);

        try {
            if (rocketMQTemplate != null) {
                SendResult sendResult = rocketMQTemplate.syncSend(
                        MqTopic.FLASH_ORDER,
                        orderMsg.toJson(),
                        3000  // 发送超时 3 秒
                );

                if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
                    log.info("MQ发送成功: requestId={}, msgId={}, cost={}ms",
                            requestId, sendResult.getMsgId(),
                            System.currentTimeMillis() - startTime);
                } else {
                    // 发送状态异常但仍可能成功（如 FLUSH_DISK_TIMEOUT）
                    log.warn("MQ发送状态异常: {}, requestId={}",
                            sendResult.getSendStatus(), requestId);
                }
            } else {
                log.warn("RocketMQ未配置，跳过消息发送");
            }
        } catch (Exception e) {
            // MQ 异常 → 库存已扣但消息可能未到 → 后续由补偿任务兜底
            log.error("MQ发送异常, 需要补偿: requestId={}, fsId={}, uid={}",
                    requestId, fsId, uid, e);
            // 不抛异常，让用户感知抢购成功
        }

        // ---- 6. 库存告警 ----
        if (stockRemain <= 10) {
            log.warn("库存即将售罄: fsId={}, remain={}", fsId, stockRemain);
        }

        result.put("code", 0);
        result.put("msg", "抢购成功，正在排队处理");
        result.put("stockRemain", stockRemain);
        result.put("requestId", requestId);
        result.put("cost", System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 库存回滚（优化版）
     */
    @Override
    @Transactional
    public boolean rollbackStock(String fsId, String uid, int qty) {
        String rollbackLua =
                "local stock = redis.call('INCR', KEYS[1]) " +
                "redis.call('SREM', KEYS[2], ARGV[1]) " +
                "redis.call('DECR', KEYS[3]) " +
                "return stock";

        List<String> keys = Arrays.asList(
                KEY_STOCK + fsId,
                KEY_USERS + fsId,
                KEY_BUY   + fsId + ":" + uid
        );

        Long newStock = redisTemplate.execute(
                new DefaultRedisScript<>(rollbackLua, Long.class), keys, uid);

        log.info("库存回滚成功: fsId={}, uid={}, newStock={}", fsId, uid, newStock);
        return true;
    }
}
