package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("draw_info")
public class DrawInfo {
    private Integer id;
    private String drawId;
    private String drawOpportunityId;
    private String activityId;
    private String uid;
    private String prizeId;
    private String prizeName;
    private Integer drawStatus;
    private Long drawTime;
    private Long writeOffeTime;
    private String writeOffeRemark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getDrawId() {
        return drawId;
    }

    public void setDrawId(String v) {
        this.drawId = v;
    }

    public String getDrawOpportunityId() {
        return drawOpportunityId;
    }

    public void setDrawOpportunityId(String v) {
        this.drawOpportunityId = v;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String v) {
        this.activityId = v;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String v) {
        this.prizeId = v;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String v) {
        this.prizeName = v;
    }

    public Integer getDrawStatus() {
        return drawStatus;
    }

    public void setDrawStatus(Integer v) {
        this.drawStatus = v;
    }

    public Long getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(Long v) {
        this.drawTime = v;
    }

    public Long getWriteOffeTime() {
        return writeOffeTime;
    }

    public void setWriteOffeTime(Long v) {
        this.writeOffeTime = v;
    }

    public String getWriteOffeRemark() {
        return writeOffeRemark;
    }

    public void setWriteOffeRemark(String v) {
        this.writeOffeRemark = v;
    }
}