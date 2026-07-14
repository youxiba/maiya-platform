package ny.shop.youxuan.marketingservice.circuitbreaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 降级配置（从 Nacos 配置中心动态刷新）
 */
@Component
@RefreshScope
public class DegradeConfig {

    private static final Logger log = LoggerFactory.getLogger(DegradeConfig.class);

    /** 全局降级开关 */
    @Value("${flash.degrade.global:false}")
    private boolean globalDegrade;

    /** 预计等待时间（毫秒） */
    @Value("${flash.degrade.estimated-wait:3000}")
    private long estimatedWaitMs;

    /** 白名单用户（逗号分隔） */
    @Value("${flash.degrade.whitelist:}")
    private String whitelist;

    private volatile Set<String> whitelistCache = new HashSet<>();

    @jakarta.annotation.PostConstruct
    public void init() {
        refreshWhitelist();
    }

    /**
     * 判断当前是否应降级
     * @param uid 用户 ID
     * @return true=降级，返回排队中；false=正常处理
     */
    public boolean shouldDegrade(String uid) {
        if (globalDegrade && !isWhitelisted(uid)) {
            return true;
        }
        return false;
    }

    /**
     * 是否需要排队（熔断时也返回排队提示）
     */
    public boolean isQueuing(boolean circuitOpen) {
        return globalDegrade || circuitOpen;
    }

    public boolean isGlobalDegrade() { return globalDegrade; }

    public void setGlobalDegrade(boolean v) {
        log.warn("全局降级状态变更: {} → {}", globalDegrade, v);
        this.globalDegrade = v;
    }

    public long getEstimatedWaitMs() { return estimatedWaitMs; }
    public String getWhitelist() { return whitelist; }

    private boolean isWhitelisted(String uid) {
        if (whitelist == null || whitelist.isEmpty()) return false;
        return whitelistCache.contains(uid);
    }

    private void refreshWhitelist() {
        if (whitelist != null && !whitelist.isEmpty()) {
            whitelistCache = new HashSet<>(Arrays.asList(whitelist.split(",")));
        } else {
            whitelistCache = new HashSet<>();
        }
    }
}
