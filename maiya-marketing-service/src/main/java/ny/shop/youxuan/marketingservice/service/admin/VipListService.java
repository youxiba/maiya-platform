package ny.shop.youxuan.marketingservice.service.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VIP 名单管理服务
 *
 * 管理秒杀系统的 VIP 用户白名单。
 * VIP 用户在 Nginx 层可跳过限流，在业务层享有优先处理权。
 *
 * VIP 名单写入 Redis，Nginx 每 5 秒同步到共享内存，即时生效。
 */
@Service
public class VipListService {

    private static final Logger log = LoggerFactory.getLogger(VipListService.class);

    private static final String REDIS_KEY = "nginx:vip_list";
    private static final String REDIS_CHANNEL = "nginx:ratelimit:sync";

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    /**
     * 添加 VIP 用户
     */
    public boolean addVip(String uid) {
        if (redisTemplate == null) return false;

        redisTemplate.opsForHash().put(REDIS_KEY, uid, "1");
        notifySync("vip_add:" + uid);

        log.info("VIP 用户已添加: uid={}", uid);
        return true;
    }

    /**
     * 批量添加 VIP（秒杀活动运营人员/测试账号）
     */
    public int batchAddVip(List<String> uids) {
        if (redisTemplate == null) return 0;

        Map<String, String> map = new HashMap<>();
        uids.forEach(uid -> map.put(uid, "1"));
        redisTemplate.opsForHash().putAll(REDIS_KEY, map);
        notifySync("vip_batch_add:" + uids.size());

        log.info("VIP 用户批量添加: count={}", uids.size());
        return uids.size();
    }

    /**
     * 批量添加 VIP（逗号分隔的字符串）
     */
    public int batchAddVipFromString(String uidStr) {
        String[] parts = uidStr.split(",");
        return batchAddVip(Arrays.asList(parts));
    }

    /**
     * 移除 VIP
     */
    public boolean removeVip(String uid) {
        if (redisTemplate == null) return false;

        redisTemplate.opsForHash().delete(REDIS_KEY, uid);
        notifySync("vip_remove:" + uid);

        log.info("VIP 用户已移除: uid={}", uid);
        return true;
    }

    /**
     * 检查是否为 VIP
     */
    public boolean isVip(String uid) {
        if (redisTemplate == null) return false;
        return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(REDIS_KEY, uid));
    }

    /**
     * 获取所有 VIP 列表
     */
    public Map<Object, Object> getAllVips() {
        if (redisTemplate == null) return new HashMap<>();
        return redisTemplate.opsForHash().entries(REDIS_KEY);
    }

    /**
     * 获取 VIP 数量
     */
    public long getVipCount() {
        if (redisTemplate == null) return 0;
        Long size = redisTemplate.opsForHash().size(REDIS_KEY);
        return size != null ? size : 0;
    }

    /**
     * 通知 Nginx 节点刷新 VIP 名单
     */
    private void notifySync(String msg) {
        try {
            redisTemplate.convertAndSend(REDIS_CHANNEL, msg);
        } catch (Exception e) {
            log.warn("通知 Nginx 同步失败: {}", e.getMessage());
        }
    }
}
