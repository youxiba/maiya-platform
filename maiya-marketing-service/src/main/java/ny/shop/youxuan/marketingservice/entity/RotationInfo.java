package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("rotation_info")
public class RotationInfo {
    private Integer id;
    private String infoId;
    private String picUrl;
    private String linkUrl;
    private Integer sort;
    private Integer rotationType;
    private Integer rotationStatus;
    private Boolean enable;

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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String v) {
        this.picUrl = v;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String v) {
        this.linkUrl = v;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer v) {
        this.sort = v;
    }

    public Integer getRotationType() {
        return rotationType;
    }

    public void setRotationType(Integer v) {
        this.rotationType = v;
    }

    public Integer getRotationStatus() {
        return rotationStatus;
    }

    public void setRotationStatus(Integer v) {
        this.rotationStatus = v;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean v) {
        this.enable = v;
    }
}