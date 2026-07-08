package ny.shop.youxuan.deliveryservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("rider_score")
public class RiderScore {
    private Integer id;
    private String infoId;
    private String rid;
    private String orderId;
    private String rater;
    private Double score;
    private String remark;
    private Long createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String v) {
        this.infoId = v;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String v) {
        this.rid = v;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String v) {
        this.orderId = v;
    }

    public String getRater() {
        return rater;
    }

    public void setRater(String v) {
        this.rater = v;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double v) {
        this.score = v;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String v) {
        this.remark = v;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long v) {
        this.createTime = v;
    }
}