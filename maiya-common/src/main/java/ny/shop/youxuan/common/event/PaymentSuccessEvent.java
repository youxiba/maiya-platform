package ny.shop.youxuan.common.event;

import java.io.Serializable;

public class PaymentSuccessEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String eventId;
    private String orderId;
    private String transactionId;
    private String payType;
    private Long payTime;

    public PaymentSuccessEvent() {
    }

    public PaymentSuccessEvent(String orderId, String tid, String pt) {
        this.orderId = orderId;
        this.transactionId = tid;
        this.payType = pt;
        this.payTime = System.currentTimeMillis();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String v) {
        this.eventId = v;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String v) {
        this.orderId = v;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String v) {
        this.transactionId = v;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String v) {
        this.payType = v;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long v) {
        this.payTime = v;
    }
}