package ny.shop.youxuan.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class RefundDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String subId;
    private String transactionId;
    private BigDecimal fee;
    private String reason;

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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String v) {
        this.transactionId = v;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal v) {
        this.fee = v;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String v) {
        this.reason = v;
    }
}