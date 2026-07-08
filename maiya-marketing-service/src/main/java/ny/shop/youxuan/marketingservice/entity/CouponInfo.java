package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("coupon_info")
public class CouponInfo {
    private Integer id;
    private String couponId;
    private String couponName;
    private Integer couponType;
    private BigDecimal parValue;
    private BigDecimal terms;
    private Long startTime;
    private Long endTime;
    private String mid;
    private String categoryId;
    private Boolean valid;
    private Integer totalNum;
    private Integer receivedNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String v) {
        this.couponId = v;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String v) {
        this.couponName = v;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer v) {
        this.couponType = v;
    }

    public BigDecimal getParValue() {
        return parValue;
    }

    public void setParValue(BigDecimal v) {
        this.parValue = v;
    }

    public BigDecimal getTerms() {
        return terms;
    }

    public void setTerms(BigDecimal v) {
        this.terms = v;
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

    public String getMid() {
        return mid;
    }

    public void setMid(String v) {
        this.mid = v;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean v) {
        this.valid = v;
    }
}