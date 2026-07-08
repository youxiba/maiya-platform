package ny.shop.youxuan.orderservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("order_info")
public class OrderInfo {
    private Integer id;
    private String orderId;
    private String subId;
    private String uid;
    private String mid;
    private String storeName;
    private String nickname;
    private String goodsName;
    private String goodsUrl;
    private Integer orderStatus;
    private Integer payType;
    private Integer dtbtType;
    private BigDecimal ahsrSum;
    private BigDecimal totalFee;
    private BigDecimal dtbtFee;
    private Long createTime;
    private Long payTime;
    private String transactionId;
    private String remark;

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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String v) {
        this.storeName = v;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String v) {
        this.nickname = v;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String v) {
        this.goodsName = v;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String v) {
        this.goodsUrl = v;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer v) {
        this.orderStatus = v;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer v) {
        this.payType = v;
    }

    public BigDecimal getAhsrSum() {
        return ahsrSum;
    }

    public void setAhsrSum(BigDecimal v) {
        this.ahsrSum = v;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String v) {
        this.transactionId = v;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long v) {
        this.createTime = v;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String v) {
        this.remark = v;
    }
}