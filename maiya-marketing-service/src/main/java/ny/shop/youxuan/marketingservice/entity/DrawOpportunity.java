package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("draw_opportunity")
public class DrawOpportunity {
    private Integer id;
    private String drawOpportunityId;
    private String uid;
    private String orderId;
    private String activityId;
    private Integer drawOpportunityStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getDrawOpportunityId() {
        return drawOpportunityId;
    }

    public void setDrawOpportunityId(String v) {
        this.drawOpportunityId = v;
    }

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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String v) {
        this.activityId = v;
    }

    public Integer getDrawOpportunityStatus() {
        return drawOpportunityStatus;
    }

    public void setDrawOpportunityStatus(Integer v) {
        this.drawOpportunityStatus = v;
    }
}