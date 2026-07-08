package ny.shop.youxuan.paymentservice.mq;

import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ny.shop.youxuan.common.constant.MqTopic;
import ny.shop.youxuan.common.event.PaymentSuccessEvent;

@Component
public class PaymentEventProducer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventProducer.class);

    @Autowired(required = false)
    private RocketMQTemplate rocketMQTemplate;

    /** 支付成功 -> 发送事件给 Order Service */
    public void sendPaymentSuccess(PaymentSuccessEvent event) {
        if (rocketMQTemplate == null) {
            log.warn("RocketMQ 未配置，跳过事件发送: {}", event.getOrderId());
            return;
        }
        try {
            rocketMQTemplate.convertAndSend(MqTopic.PAYMENT_SUCCESS, event);
            log.info("发送支付成功事件: orderId={}, transactionId={}",
                    event.getOrderId(), event.getTransactionId());
        } catch (Exception e) {
            log.error("发送支付成功事件失败: orderId=" + event.getOrderId(), e);
        }
    }
}