package ny.shop.youxuan.merchantservice.entity;

import com.baomidou.mybatisplus.annotation.*;

@TableName("merchant_hours")
public class MerchantHours {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String uid;
    private String workDay;
    private Long startTime1;
    private Long endTime1;
    private Long startTime2;
    private Long endTime2;
    private Integer workStatus;
    private Boolean openFlag;

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

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer v) {
        this.workStatus = v;
    }

    public Boolean getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Boolean v) {
        this.openFlag = v;
    }
}