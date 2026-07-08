package ny.shop.youxuan.orderservice.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ny.shop.youxuan.common.constant.MqTopic;
import ny.shop.youxuan.common.event.OrderPaidEvent;
import ny.shop.youxuan.common.event.PaymentSuccessEvent;
import ny.shop.youxuan.orderservice.service.OrderService;

@Component
@RocketMQMessageListener(topic = MqTopic.PAYMENT_SUCCESS,
                         consumerGroup = MqTopic.CONSUMER_ORDER)
public class OrderEventConsumer implements RocketMQListener<String> {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    @Autowired private OrderService orderService;
    @Autowired(required = false) private RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(String message) {
        try {
            JSONObject json = JSON.parseObject(message);
            String orderId = json.getString("orderId");
            String transactionId = json.getString("transactionId");
            String payType = json.getString("payType");

            log.info("收到支付成功事件: orderId={}, transactionId={}", orderId, transactionId);

            // 处理支付回调
            boolean success = orderService.processPayment(orderId, transactionId, Integer.parseInt(payType));

            if (success && rocketMQTemplate != null) {
                // 发布 OrderPaidEvent 给下游服务
                OrderPaidEvent paidEvent = new OrderPaidEvent();
                paidEvent.setOrderId(orderId);
                paidEvent.setPayType(payType);
                paidEvent.setTransactionId(transactionId);
                paidEvent.setPayTime(json.getLong("payTime"));
                rocketMQTemplate.convertAndSend(MqTopic.ORDER_PAID, paidEvent);
                log.info("发送 OrderPaidEvent: orderId={}", orderId);
            }
        } catch (Exception e) {
            log.error("处理支付成功事件失败", e);
        }
    }
}