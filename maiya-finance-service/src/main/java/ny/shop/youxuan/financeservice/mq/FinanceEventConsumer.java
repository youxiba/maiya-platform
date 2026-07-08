package ny.shop.youxuan.financeservice.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ny.shop.youxuan.common.constant.MqTopic;
import ny.shop.youxuan.financeservice.service.ProfitDistributionService;

@Component
@RocketMQMessageListener(topic = MqTopic.ORDER_PAID,
                         consumerGroup = MqTopic.CONSUMER_FINANCE)
public class FinanceEventConsumer implements RocketMQListener<String> {

    private static final Logger log = LoggerFactory.getLogger(FinanceEventConsumer.class);

    @Autowired private ProfitDistributionService profitService;

    @Override
    public void onMessage(String message) {
        try {
            JSONObject json = JSON.parseObject(message);
            String orderId = json.getString("orderId");
            log.info("收到 OrderPaidEvent: orderId={}", orderId);

            // TODO: 记录账户流水（调用 ProfitDistributionService）
            // profitService.settleOrder(orderId, ...);

            log.info("订单记账完成: orderId={}", orderId);
        } catch (Exception e) {
            log.error("处理订单事件失败", e);
        }
    }
}