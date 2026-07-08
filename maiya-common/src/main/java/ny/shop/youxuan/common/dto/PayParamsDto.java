package ny.shop.youxuan.common.dto;

import ny.shop.youxuan.common.enums.PayType;
import java.io.Serializable;

public class PayParamsDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String uid;
    private String orderId;
    private PayType payType;
    private Integer fee;
    private String notifyUrl;

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String v) {
        this.orderId = v;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType v) {
        this.payType = v;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer v) {
        this.fee = v;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String v) {
        this.notifyUrl = v;
    }
}