package ny.shop.youxuan.common.dto;

import ny.shop.youxuan.common.enums.PayType;
import java.io.Serializable;

public class PayNotifyDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private PayType payType;
    private String orderId;
    private String transactionId;
    private Boolean flag;

    public PayNotifyDto() {
    }

    public PayNotifyDto(PayType p, String o, String t, Boolean f) {
        this.payType = p;
        this.orderId = o;
        this.transactionId = t;
        this.flag = f;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType v) {
        this.payType = v;
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

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean v) {
        this.flag = v;
    }
}