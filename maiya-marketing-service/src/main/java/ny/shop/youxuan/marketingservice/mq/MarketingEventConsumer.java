package ny.shop.youxuan.marketingservice.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ny.shop.youxuan.common.constant.MqTopic;

@Component
@RocketMQMessageListener(topic = MqTopic.ORDER_PAID,
                         consumerGroup = MqTopic.CONSUMER_MARKETING)
public class MarketingEventConsumer implements RocketMQListener<String> {

    private static final Logger log = LoggerFactory.getLogger(MarketingEventConsumer.class);

    @Override
    public void onMessage(String message) {
        try {
            JSONObject json = JSON.parseObject(message);
            String orderId = json.getString("orderId");
            String uid = json.getString("uid");
            log.info("收到 OrderPaidEvent 赠送抽奖机会: orderId={}, uid={}", orderId, uid);

            // TODO: 查询是否有进行中的活动，赠送一次抽奖机会
            // DrawOpportunity opportunity = new DrawOpportunity();
            // opportunity.setUid(uid);
            // opportunity.setOrderId(orderId);
            // drawOpportunityMapper.insert(opportunity);

            log.info("抽奖机会赠送完成: uid={}", uid);
        } catch (Exception e) {
            log.error("赠送抽奖机会失败", e);
        }
    }
}