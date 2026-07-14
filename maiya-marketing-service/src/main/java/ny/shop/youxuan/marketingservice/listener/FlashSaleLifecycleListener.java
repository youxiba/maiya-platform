package ny.shop.youxuan.marketingservice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ny.shop.youxuan.marketingservice.entity.FlashSale;
import ny.shop.youxuan.marketingservice.mapper.FlashSaleMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 秒杀生命周期监听器
 *
 * 自动管理秒杀活动全生命周期的限流策略：
 * - 活动开始前 1 分钟 → 放宽限流，预热缓存
 * - 活动期间 → 维持大流量限流
 * - 活动结束后 → 恢复默认限流
 *
 * 通过 Redis 通知 Nginx 更新共享内存中的限流配置，无需 reload。
 */
@Component
public class FlashSaleLifecycleListener {

    private static final Logger log = LoggerFactory.getLogger(FlashSaleLifecycleListener.class);

    private static final String REDIS_KEY_PREFIX = "nginx:ratelimit:";
    private static final String REDIS_CHANNEL = "nginx:ratelimit:sync";

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Autowired
    private FlashSaleMapper flashSaleMapper;

    // 上次已通知的活动状态
    private final Map<String, Boolean> notifiedActive = new HashMap<>();

    /**
     * 每 10 秒检查一次秒杀活动状态，自动调整限流
     */
    @Scheduled(fixedRate = 10000)
    public void checkFlashSaleLifecycle() {
        if (redisTemplate == null) return;

        try {
            List<FlashSale> activeSales = flashSaleMapper.findActive();
            long now = System.currentTimeMillis();

            for (FlashSale sale : activeSales) {
                String fsId = sale.getFsId();
                Boolean wasActive = notifiedActive.getOrDefault(fsId, false);
                boolean isActive = isInFlashWindow(sale, now);

                if (isActive && !wasActive) {
                    // 活动开始 → 放宽限流
                    onFlashStart(fsId);
                    notifiedActive.put(fsId, true);
                } else if (!isActive && wasActive) {
                    // 活动结束 → 恢复默认
                    onFlashEnd(fsId);
                    notifiedActive.put(fsId, false);
                }
            }

            // 清理已结束的活动记录
            notifiedActive.keySet().removeIf(key ->
                    activeSales.stream().noneMatch(s -> s.getFsId().equals(key)));

        } catch (Exception e) {
            log.error("检查秒杀活动状态异常", e);
        }
    }

    /**
     * 判断当前是否在秒杀窗口内
     */
    private boolean isInFlashWindow(FlashSale sale, long now) {
        return sale.getStartTime() != null
                && sale.getEndTime() != null
                && now >= sale.getStartTime()
                && now <= sale.getEndTime()
                && Boolean.TRUE.equals(sale.getEnable());
    }

    /**
     * 秒杀活动开始 → 放宽限流
     */
    private void onFlashStart(String fsId) {
        Map<String, String> config = new HashMap<>();
        config.put("user_qps", "2000");          // 用户限流 1000→2000
        config.put("total_qps", "10000");         // 总入口 5000→10000
        config.put("ip_burst", "10");             // IP 突发 5→10
        config.put("downgrade_rate", "1.0");      // 不降级

        config.forEach((k, v) ->
                redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + k, v));
        redisTemplate.convertAndSend(REDIS_CHANNEL, "flash_start:" + fsId);

        log.warn("秒杀活动开始, 放宽限流: fsId={}", fsId);
    }

    /**
     * 秒杀活动结束 → 恢复默认限流
     */
    private void onFlashEnd(String fsId) {
        Map<String, String> config = new HashMap<>();
        config.put("user_qps", "1000");
        config.put("total_qps", "5000");
        config.put("ip_burst", "5");
        config.put("downgrade_rate", "0.5");      // 活动后适度降级，保护后端

        config.forEach((k, v) ->
                redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + k, v));
        redisTemplate.convertAndSend(REDIS_CHANNEL, "flash_end:" + fsId);

        log.warn("秒杀活动结束, 恢复限流: fsId={}", fsId);
    }
}
