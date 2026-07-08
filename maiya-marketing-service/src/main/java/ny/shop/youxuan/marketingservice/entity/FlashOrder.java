package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

/** 秒杀订单（独立表，高频写入） */
@TableName("flash_order")
public class FlashOrder {
    private Long id;
    private String orderId;
    private String fsId;
    private String uid;
    private String goodsInfoId;
    private BigDecimal flashPrice;
    private Integer qty;
    private Integer orderStatus; // 0=待支付 1=已支付 2=已取消
    private Long createTime;
    private Long payTime;

    public Long getId() {
        return id;
    }

    public void setId(Long v) {
        this.id = v;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String v) {
        this.orderId = v;
    }

    public String getFsId() {
        return fsId;
    }

    public void setFsId(String v) {
        this.fsId = v;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public String getGoodsInfoId() {
        return goodsInfoId;
    }

    public void setGoodsInfoId(String v) {
        this.goodsInfoId = v;
    }

    public BigDecimal getFlashPrice() {
        return flashPrice;
    }

    public void setFlashPrice(BigDecimal v) {
        this.flashPrice = v;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer v) {
        this.qty = v;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer v) {
        this.orderStatus = v;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long v) {
        this.createTime = v;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long v) {
        this.payTime = v;
    }
}
