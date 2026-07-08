package ny.shop.youxuan.common.constant;

/**
 * MQ 主题常量
 */
public interface MqTopic {

    /** 支付成功事件 */
    String PAYMENT_SUCCESS = "PAYMENT_SUCCESS";

    /** 订单支付完成事件 */
    String ORDER_PAID = "ORDER_PAID";

    /** 订单退款事件 */
    String ORDER_REFUND = "ORDER_REFUND";

    /** 订单完成事件 */
    String ORDER_COMPLETED = "ORDER_COMPLETED";

    /** 短信通知 */
    String SMS_NOTIFY = "SMS_NOTIFY";

    /** 推送通知 */
    String PUSH_NOTIFY = "PUSH_NOTIFY";

    /** 打印通知 */
    String PRINT_NOTIFY = "PRINT_NOTIFY";

    /** 秒杀订单请求 */
    String FLASH_ORDER = "FLASH_ORDER";

    // ===== 消费者组 =====
    String CONSUMER_ORDER = "consumer-order";
    String CONSUMER_FINANCE = "consumer-finance";
    String CONSUMER_DELIVERY = "consumer-delivery";
    String CONSUMER_NOTIFICATION = "consumer-notification";
    String CONSUMER_MARKETING = "consumer-marketing";
}
