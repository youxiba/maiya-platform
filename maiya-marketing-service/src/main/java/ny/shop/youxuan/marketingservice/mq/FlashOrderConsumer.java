package ny.shop.youxuan.marketingservice.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ny.shop.youxuan.common.constant.MqTopic;
import ny.shop.youxuan.marketingservice.entity.FlashOrder;
import ny.shop.youxuan.marketingservice.mapper.FlashOrderMapper;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * 秒杀订单消费者 - 异步创建秒杀订单
 *
 * 顺序消费保证同一商品订单串行处理，防止库存超卖
 */
@Component
@RocketMQMessageListener(
    topic = MqTopic.FLASH_ORDER,
    consumerGroup = "consumer-flash-order",
    consumeMode = org.apache.rocketmq.spring.annotation.ConsumeMode.ORDERLY
)
public class FlashOrderConsumer implements RocketMQListener<String> {

    private static final Logger log = LoggerFactory.getLogger(FlashOrderConsumer.class);

    @Autowired
    private FlashOrderMapper flashOrderMapper;

    @Override
    @Transactional
    public void onMessage(String message) {
        try {
            JSONObject msg = JSON.parseObject(message);
            String fsId = msg.getString("fsId");
            String uid = msg.getString("uid");
            String requestId = msg.getString("requestId");

            log.info("收到秒杀订单请求: fsId={}, uid={}", fsId, uid);

            // 1. 幂等校验 - 防止重复下单
            FlashOrder existing = flashOrderMapper.findPendingOrder(uid, fsId);
            if (existing != null) {
                log.warn("重复秒杀请求: fsId={}, uid={}", fsId, uid);
                return;
            }

            // 2. 创建秒杀订单
            FlashOrder order = new FlashOrder();
            order.setOrderId(UUID.randomUUID().toString().replace("-", ""));
            order.setFsId(fsId);
            order.setUid(uid);
            order.setQty(1);
            order.setOrderStatus(0); // 待支付
            order.setCreateTime(System.currentTimeMillis());

            // 从 Redis 或 DB 获取秒杀价
            order.setFlashPrice(new BigDecimal("0.01"));
            order.setGoodsInfoId(fsId);

            flashOrderMapper.insert(order);

            log.info("秒杀订单创建成功: orderId={}, fsId={}, uid={}", order.getOrderId(), fsId, uid);

        } catch (Exception e) {
            log.error("秒杀订单创建失败: {}", message, e);
        }
    }
}
