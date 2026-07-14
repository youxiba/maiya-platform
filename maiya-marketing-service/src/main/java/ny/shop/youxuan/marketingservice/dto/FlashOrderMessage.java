package ny.shop.youxuan.marketingservice.dto;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.Accessors;
import lombok.experimental.Accessors;

/**
 * 秒杀订单 MQ 消息体
 *
 * 封装抢购成功后投递到 RocketMQ 的订单消息。
 * 包含 requestId 实现端到端幂等。
 */
@Data
@Accessors(chain = true)
public class FlashOrderMessage {

    /** 秒杀活动 ID */
    private String fsId;

    /** 用户 ID */
    private String uid;

    /** 请求幂等 ID（由前端生成或网关注入） */
    private String requestId;

    /** 消息发送时间戳 */
    private long timestamp;

    /** 重试次数 */
    private int retryCount;

    /** 原始 Topic（延迟消息回源时使用） */
    private String originTopic;

    /** 消息来源标识 */
    private String source;

    public static FlashOrderMessage create(String fsId, String uid, String requestId) {
        return new FlashOrderMessage()
                .setFsId(fsId)
                .setUid(uid)
                .setRequestId(requestId)
                .setTimestamp(System.currentTimeMillis())
                .setSource("flash-sale")
                .setRetryCount(0);
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }
}
