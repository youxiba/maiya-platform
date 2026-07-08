package ny.shop.youxuan.common.dto;

import java.io.Serializable;

public class MerchantDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mid;
    private String uid;
    private String storeName;
    private String telephone;
    private String logo;
    private Double lon;
    private Double lat;
    private Boolean online;

    public String getMid() {
        return mid;
    }

    public void setMid(String v) {
        this.mid = v;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String v) {
        this.logo = v;
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
}