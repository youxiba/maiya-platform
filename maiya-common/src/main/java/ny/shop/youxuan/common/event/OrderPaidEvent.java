package ny.shop.youxuan.common.event;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderPaidEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String subId;
    private String uid;
    private String mid;
    private String payType;
    private String transactionId;
    private BigDecimal ahsrSum;
    private Long payTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String v) {
        this.orderId = v;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String v) {
        this.subId = v;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String v) {
        this.mid = v;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String v) {
        this.payType = v;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String v) {
        this.transactionId = v;
    }

    public BigDecimal getAhsrSum() {
        return ahsrSum;
    }

    public void setAhsrSum(BigDecimal v) {
        this.ahsrSum = v;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long v) {
        this.payTime = v;
    }
}