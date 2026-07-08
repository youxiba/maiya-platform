package ny.shop.youxuan.marketingservice.compensation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ny.shop.youxuan.marketingservice.entity.FlashOrder;
import ny.shop.youxuan.marketingservice.mapper.FlashOrderMapper;
import ny.shop.youxuan.marketingservice.service.FlashSaleHighConcurrencyService;

import java.util.List;

/**
 * 秒杀补偿任务
 *
 * 1. 超时未支付秒杀订单 → 回滚库存
 * 2. 未预热活动 → 自动预热
 */
@Component
public class FlashCompensationTask {

    private static final Logger log = LoggerFactory.getLogger(FlashCompensationTask.class);
    private static final long FLASH_PAY_TIMEOUT = 10 * 60 * 1000L; // 秒杀订单10分钟未支付自动取消

    @Autowired
    private FlashOrderMapper flashOrderMapper;

    @Autowired
    private FlashSaleHighConcurrencyService flashSaleService;

    /** 每分钟扫描超时未支付秒杀订单 */
    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void cancelTimeoutFlashOrders() {
        long deadline = System.currentTimeMillis() - FLASH_PAY_TIMEOUT;
        List<FlashOrder> timeoutOrders = flashOrderMapper.findTimeoutOrders(deadline);

        for (FlashOrder order : timeoutOrders) {
            // 取消订单
            flashOrderMapper.updateStatus(order.getOrderId(), 2); // 2=已取消

            // 回滚 Redis 库存
            flashSaleService.rollbackStock(order.getFsId(), order.getUid(), order.getQty());

            log.info("秒杀超时取消: orderId={}, uid={}, fsId={}", order.getOrderId(), order.getUid(), order.getFsId());
        }

        if (!timeoutOrders.isEmpty()) {
            log.info("本次取消超时秒杀订单: {} 笔", timeoutOrders.size());
        }
    }
}
