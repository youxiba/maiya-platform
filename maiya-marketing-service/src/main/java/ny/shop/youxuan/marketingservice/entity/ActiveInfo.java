package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("active_info")
public class ActiveInfo {
    private Integer id;
    private String activeId;
    private String activeName;
    private Integer activeType;
    private Integer activeStatus;
    private Long startDate;
    private Long endDate;
    private Long startTime;
    private Long endTime;
    private Boolean timeFlag;
    private Boolean enable;
    private String activeDesc;
    private Integer limitSum;
    private String pic;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String v) {
        this.activeId = v;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String v) {
        this.activeName = v;
    }

    public Integer getActiveType() {
        return activeType;
    }

    public void setActiveType(Integer v) {
        this.activeType = v;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer v) {
        this.activeStatus = v;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long v) {
        this.startDate = v;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long v) {
        this.endDate = v;
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

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean v) {
        this.enable = v;
    }

    public Boolean getTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(Boolean v) {
        this.timeFlag = v;
    }

    public String getActiveDesc() {
        return activeDesc;
    }

    public void setActiveDesc(String v) {
        this.activeDesc = v;
    }

    public Integer getLimitSum() {
        return limitSum;
    }

    public void setLimitSum(Integer v) {
        this.limitSum = v;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String v) {
        this.pic = v;
    }
}