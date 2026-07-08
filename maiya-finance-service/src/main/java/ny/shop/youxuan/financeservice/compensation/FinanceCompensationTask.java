package ny.shop.youxuan.financeservice.compensation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 财务补偿任务
 *
 * 1. 未结算订单重试
 * 2. 分润失败重试
 */
@Component
public class FinanceCompensationTask {

    private static final Logger log = LoggerFactory.getLogger(FinanceCompensationTask.class);

    /** 每10分钟扫描未结算订单 */
    @Scheduled(fixedRate = 600_000)
    public void retrySettlement() {
        // TODO: 扫描 account_info 中缺失的订单结算记录，重新执行分润计算
        log.info("结算重试检查完成");
    }
}
