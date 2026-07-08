package ny.shop.youxuan.paymentservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("payment_record")
public class PaymentRecord {
    private Integer id;
    private String orderId;
    private String transactionId;
    private String payType;
    private BigDecimal fee;
    private String tradeStatus;
    private Long createTime;
    private Long notifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
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

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal v) {
        this.fee = v;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String v) {
        this.tradeStatus = v;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long v) {
        this.createTime = v;
    }

    public Long getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Long v) {
        this.notifyTime = v;
    }
}
