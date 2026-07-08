package ny.shop.youxuan.orderservice.compensation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ny.shop.youxuan.orderservice.entity.OrderInfo;
import ny.shop.youxuan.orderservice.mapper.OrderInfoMapper;
import ny.shop.youxuan.orderservice.statemachine.OrderStatus;

import java.util.List;

/**
 * 订单补偿定时任务
 *
 * 1. 超时未支付订单 → 作废（30分钟）
 * 2. 已支付但配送超时 → 提醒
 * 3. 退款中的订单超时 → 自动退款
 */
@Component
public class OrderCompensationTask {

    private static final Logger log = LoggerFactory.getLogger(OrderCompensationTask.class);
    private static final long PAYMENT_TIMEOUT = 30 * 60 * 1000L; // 30分钟
    private static final long CONFIRM_TIMEOUT = 7 * 24 * 3600 * 1000L; // 7天自动确认

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    /** 每5分钟扫描超时未支付订单 */
    @Scheduled(fixedRate = 300_000)
    @Transactional
    public void closeTimeoutOrders() {
        long deadline = System.currentTimeMillis() - PAYMENT_TIMEOUT;
        List<OrderInfo> timeoutOrders = orderInfoMapper.findTimeoutOrders(
                OrderStatus.ORDER.getCode(), deadline);

        int count = 0;
        for (OrderInfo order : timeoutOrders) {
            orderInfoMapper.updateStatus(order.getOrderId(),
                    OrderStatus.ORDER.getCode(), OrderStatus.USELESS.getCode());
            count++;
            log.info("超时关闭订单: orderId={}", order.getOrderId());
        }

        if (count > 0) {
            log.info("本次关闭超时订单: {} 笔", count);
        }
    }

    /**
     * Saga 补偿 - 支付失败回滚
     * 当订单支付处理失败时调用。场景：
     * 1. Payment Service 已扣款
     * 2. Order Service 更新订单状态失败
     * 3. 需要通知 Payment Service 退款
     */
    @Transactional
    public void compensatePaymentFailure(String orderId) {
        log.warn("订单支付失败补偿: orderId={}", orderId);

        OrderInfo order = getOrder(orderId);
        if (order == null) {
            log.warn("订单不存在，跳过补偿: orderId={}", orderId);
            return;
        }

        // 如果订单已经是 PAID 状态，需要回滚为 ORDER 并退款
        if (order.getOrderStatus() == OrderStatus.PAID.getCode()) {
            orderInfoMapper.updateStatus(orderId,
                    OrderStatus.PAID.getCode(), OrderStatus.USELESS.getCode());

            // TODO: 通过 MQ 发送退款请求到 Payment Service
            // compensationProducer.sendRefundRequest(orderId, order.getTransactionId());

            log.info("订单已回滚为作废状态，等待退款处理: orderId={}", orderId);
        }
    }

    private OrderInfo getOrder(String orderId) {
        List<OrderInfo> list = orderInfoMapper.findByOrderId(orderId);
        return list.isEmpty() ? null : list.get(0);
    }
}
