package ny.shop.youxuan.deliveryservice.compensation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ny.shop.youxuan.deliveryservice.entity.DtbtInfo;
import ny.shop.youxuan.deliveryservice.mapper.DtbtInfoMapper;

import java.util.List;

/**
 * 配送补偿任务
 *
 * 1. 长时间未接单配送单 → 重新分配
 * 2. 配送超时提醒
 */
@Component
public class DeliveryCompensationTask {

    private static final Logger log = LoggerFactory.getLogger(DeliveryCompensationTask.class);
    private static final long PICKUP_TIMEOUT = 30 * 60 * 1000L; // 30分钟未接单

    @Autowired
    private DtbtInfoMapper dtbtInfoMapper;

    /** 每5分钟扫描未接单配送单 */
    @Scheduled(fixedRate = 300_000)
    public void retryPendingDeliveries() {
        // TODO: 扫描 dtbt_status=0(待接单) 且 create_time 超过30分钟的配送单
        // 发送重新分配通知
        log.info("配送重试检查完成");
    }
}
