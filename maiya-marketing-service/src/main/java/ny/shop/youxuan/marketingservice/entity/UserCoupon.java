package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("user_coupon")
public class UserCoupon {
    private Integer id;
    private String uid;
    private String couponId;
    private String orderId;
    private String couponName;
    private BigDecimal parValue;
    private Integer couponType;
    private Integer couponStatus;
    private Long startTime;
    private Long endTime;
    private Long useTime;
    private Long createTime;
    private String mid;
    private String categoryId;
    private Boolean valid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String v) {
        this.couponId = v;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String v) {
        this.orderId = v;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String v) {
        this.couponName = v;
    }

    public BigDecimal getParValue() {
        return parValue;
    }

    public void setParValue(BigDecimal v) {
        this.parValue = v;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer v) {
        this.couponType = v;
    }

    public Integer getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(Integer v) {
        this.couponStatus = v;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long v) {
        this.startTime = v;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long v) {
        this.endTime = v;
    }

    public Long getUseTime() {
        return useTime;
    }

    public void setUseTime(Long v) {
        this.useTime = v;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long v) {
        this.createTime = v;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean v) {
        this.valid = v;
    }
}