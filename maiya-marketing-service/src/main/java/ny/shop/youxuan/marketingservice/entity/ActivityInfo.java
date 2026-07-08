package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("activity_info")
public class ActivityInfo {
    private Integer id;
    private String activityId;
    private String activityName;
    private Integer activityStatusType;
    private Long activityCreateTime;
    private Long activityEndTime;
    private String activityDesc;
    private String shareTitle;
    private String shareLetter;
    private String shareImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String v) {
        this.activityId = v;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String v) {
        this.activityName = v;
    }

    public Integer getActivityStatusType() {
        return activityStatusType;
    }

    public void setActivityStatusType(Integer v) {
        this.activityStatusType = v;
    }

    public Long getActivityCreateTime() {
        return activityCreateTime;
    }

    public void setActivityCreateTime(Long v) {
        this.activityCreateTime = v;
    }

    public Long getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(Long v) {
        this.activityEndTime = v;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String v) {
        this.activityDesc = v;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String v) {
        this.shareTitle = v;
    }

    public String getShareLetter() {
        return shareLetter;
    }

    public void setShareLetter(String v) {
        this.shareLetter = v;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String v) {
        this.shareImage = v;
    }
}