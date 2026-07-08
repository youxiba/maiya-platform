package ny.shop.youxuan.deliveryservice.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ny.shop.youxuan.common.constant.MqTopic;
import ny.shop.youxuan.deliveryservice.entity.DtbtInfo;
import ny.shop.youxuan.deliveryservice.mapper.DtbtInfoMapper;

@Component
@RocketMQMessageListener(topic = MqTopic.ORDER_PAID,
                         consumerGroup = MqTopic.CONSUMER_DELIVERY)
public class DeliveryEventConsumer implements RocketMQListener<String> {

    private static final Logger log = LoggerFactory.getLogger(DeliveryEventConsumer.class);

    @Autowired private DtbtInfoMapper dtbtInfoMapper;

    @Override
    public void onMessage(String message) {
        try {
            JSONObject json = JSON.parseObject(message);
            String orderId = json.getString("orderId");
            String mid = json.getString("mid");
            log.info("收到 OrderPaidEvent 创建配送单: orderId={}", orderId);

            // 创建配送单（仅平台配送才创建）
            DtbtInfo delivery = new DtbtInfo();
            delivery.setOrderId(orderId);
            delivery.setMid(mid);
            delivery.setDtbtStatus(0); // 待接单
            dtbtInfoMapper.insert(delivery);

            log.info("配送单创建完成: orderId={}", orderId);
        } catch (Exception e) {
            log.error("创建配送单失败", e);
        }
    }
}