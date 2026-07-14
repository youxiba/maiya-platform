package ny.shop.youxuan.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Nginx 动态限流管理 API
 *
 * 将限流配置写入 Redis，Nginx worker 进程每 5 秒
 * 从 Redis 同步到共享内存，即时生效无需 reload。
 *
 * 支持的操作：
 * - 实时调整限流参数（QPS/突发/降级系数）
 * - 秒杀活动开始/结束时自动调优
 * - 查询当前限流状态
 */
@RestController
@RequestMapping("/admin/nginx/ratelimit")
public class NginxRatelimitController {

    private static final Logger log = LoggerFactory.getLogger(NginxRatelimitController.class);

    private static final String REDIS_KEY_PREFIX = "nginx:ratelimit:";
    private static final String REDIS_CHANNEL = "nginx:ratelimit:sync";

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    /**
     * 更新限流配置
     *
     * 请求体示例：
     * {
     *   "user_qps": "2000",
     *   "total_qps": "10000",
     *   "downgrade_rate": "1.0"
     * }
     */
    @PostMapping("/update")
    public ApiResult<String> updateRatelimit(@RequestBody Map<String, Object> config) {
        if (redisTemplate == null) {
            return ApiResult.error("Redis 未配置");
        }

        int updated = 0;
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            String key = REDIS_KEY_PREFIX + entry.getKey();
            String value = String.valueOf(entry.getValue());
            redisTemplate.opsForValue().set(key, value);
            updated++;
        }

        // 通知所有 Nginx 节点立即同步
        redisTemplate.convertAndSend(REDIS_CHANNEL, "refresh");

        log.info("限流配置已更新: {} 项, keys={}", updated, config.keySet());
        return ApiResult.success("配置已更新，等待 Nginx 同步");
    }

    /**
     * 秒杀活动开始时自动调优
     * 放宽限流阈值，应对洪峰流量
     */
    @PostMapping("/flash/start")
    public ApiResult<String> onFlashStart(@RequestParam String fsId) {
        Map<String, Object> flashConfig = new HashMap<>();
        flashConfig.put("user_qps", "2000");        // 用户限流放宽一倍
        flashConfig.put("total_qps", "10000");      // 总入口放宽一倍
        flashConfig.put("downgrade_rate", "1.0");   // 不降级
        flashConfig.put("ip_burst", "10");          // IP 突发放宽

        log.info("秒杀活动开始, 放宽限流: fsId={}", fsId);
        return updateRatelimit(flashConfig);
    }

    /**
     * 秒杀活动结束时恢复默认
     */
    @PostMapping("/flash/end")
    public ApiResult<String> onFlashEnd(@RequestParam String fsId) {
        Map<String, Object> defaultConfig = new HashMap<>();
        defaultConfig.put("user_qps", "1000");
        defaultConfig.put("total_qps", "5000");
        defaultConfig.put("downgrade_rate", "1.0");
        defaultConfig.put("ip_burst", "5");

        log.info("秒杀活动结束, 恢复限流: fsId={}", fsId);
        return updateRatelimit(defaultConfig);
    }

    /**
     * 查询当前限流配置状态
     */
    @GetMapping("/status")
    public ApiResult<Map<String, String>> getRatelimitStatus() {
        Map<String, String> status = new HashMap<>();

        if (redisTemplate == null) {
            return ApiResult.success(status);
        }

        String[] keys = {"user_qps", "ip_qps", "total_qps",
                         "downgrade_rate", "ip_burst", "user_burst"};

        for (String key : keys) {
            String val = redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + key);
            status.put(key, val != null ? val : "(未设置)");
        }

        return ApiResult.success(status);
    }

    /**
     * 手动降级（快速开启/关闭降级模式）
     */
    @PostMapping("/degrade")
    public ApiResult<String> setDegrade(@RequestParam double rate) {
        if (rate < 0 || rate > 1.0) {
            return ApiResult.error("降级系数必须在 0.0 ~ 1.0 之间");
        }

        Map<String, Object> config = new HashMap<>();
        config.put("downgrade_rate", String.valueOf(rate));
        log.warn("手动降级: rate={}", rate);
        return updateRatelimit(config);
    }
}
