package ny.shop.youxuan.merchantservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;

@TableName("merchant_info")
public class MerchantInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String uid;
    private String mid;
    private String storeName;
    private String telephone;
    private String username;
    private String mchName;
    private Long createTime;
    private Long applyTime;
    private String province;
    private String city;
    private String district;
    private String address;
    private Double lon;
    private Double lat;
    private Integer storeType;
    private String goodsCategoryId;
    private String logo;
    private String bsLicensePic;
    private String foodLicensePic;
    private Integer auditStatus;
    private Boolean online;
    private Integer ratio;
    private BigDecimal bond;
    private Integer points;
    private Integer dtbtType;
    private String dtbtId;
    private Boolean jinxuanFlag;
    private Integer jinxuanStatus;
    private Long jinxuanEndTime;

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

    public String getMid() {
        return mid;
    }

    public void setMid(String v) {
        this.mid = v;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String v) {
        this.storeName = v;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String v) {
        this.telephone = v;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double v) {
        this.lon = v;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double v) {
        this.lat = v;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean v) {
        this.online = v;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer v) {
        this.auditStatus = v;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String v) {
        this.logo = v;
    }
}