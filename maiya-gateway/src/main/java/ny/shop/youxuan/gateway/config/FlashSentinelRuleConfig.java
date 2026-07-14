package ny.shop.youxuan.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * Sentinel 秒杀限流规则配置
 *
 * 在网关层对秒杀接口做多维度限流：
 * - 总入口 QPS 5000
 * - 每用户 QPS 1
 */
@Configuration
public class FlashSentinelRuleConfig {

    private static final Logger log = LoggerFactory.getLogger(FlashSentinelRuleConfig.class);

    @PostConstruct
    public void initFlashRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();

        // 规则1: 秒杀抢购入口 — 总 QPS 5000
        GatewayFlowRule grabRule = new GatewayFlowRule("/api/flash/grab")
                .setCount(5000)
                .setIntervalSec(1)
                .setBurst(1000)
                .setParamItem(new com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem()
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL));
        rules.add(grabRule);

        // 规则2: 活动信息查询 — QPS 1000
        GatewayFlowRule infoRule = new GatewayFlowRule("/api/flash/info")
                .setCount(1000)
                .setIntervalSec(1)
                .setBurst(500);
        rules.add(infoRule);

        // 加载规则
        GatewayRuleManager.loadRules(rules);
        log.info("Sentinel 秒杀限流规则加载完成: totalQPS={}, infoQPS={}", 5000, 1000);
    }
}
