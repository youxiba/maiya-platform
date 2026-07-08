package ny.shop.youxuan.notificationservice.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ny.shop.youxuan.common.constant.MqTopic;
import ny.shop.youxuan.notificationservice.service.NotificationService;

@Component
@RocketMQMessageListener(topic = MqTopic.ORDER_PAID,
                         consumerGroup = MqTopic.CONSUMER_NOTIFICATION)
public class NotificationEventConsumer implements RocketMQListener<String> {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventConsumer.class);

    @Autowired private NotificationService notificationService;

    @Override
    public void onMessage(String message) {
        try {
            JSONObject json = JSON.parseObject(message);
            String orderId = json.getString("orderId");
            String mid = json.getString("mid");
            log.info("收到 OrderPaidEvent 打印小票: orderId={}", orderId);

            // 异步打印小票
            notificationService.printTicket(orderId, mid);

            log.info("小票打印请求完成: orderId={}", orderId);
        } catch (Exception e) {
            log.error("打印小票失败", e);
        }
    }
}