package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("prize_info")
public class PrizeInfo {
    private Integer id;
    private String prizeId;
    private String couponId;
    private String activityId;
    private Integer prizeType;
    private String prizeName;
    private String prizeImage;
    private Integer prizeNumbers;
    private Boolean writeOfferFlag;
    private Double probability;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String v) {
        this.prizeId = v;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String v) {
        this.couponId = v;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String v) {
        this.activityId = v;
    }

    public Integer getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Integer v) {
        this.prizeType = v;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String v) {
        this.prizeName = v;
    }

    public String getPrizeImage() {
        return prizeImage;
    }

    public void setPrizeImage(String v) {
        this.prizeImage = v;
    }

    public Integer getPrizeNumbers() {
        return prizeNumbers;
    }

    public void setPrizeNumbers(Integer v) {
        this.prizeNumbers = v;
    }

    public Boolean getWriteOfferFlag() {
        return writeOfferFlag;
    }

    public void setWriteOfferFlag(Boolean v) {
        this.writeOfferFlag = v;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double v) {
        this.probability = v;
    }
}