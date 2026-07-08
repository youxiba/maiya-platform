package ny.shop.youxuan.deliveryservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("rider_info")
public class RiderInfo {
    private Integer id;
    private String rid;
    private String uid;
    private String realname;
    private String telephone;
    private String stationId;
    private Integer auditStatus;
    private Boolean online;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String v) {
        this.rid = v;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String v) {
        this.realname = v;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String v) {
        this.telephone = v;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String v) {
        this.stationId = v;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer v) {
        this.auditStatus = v;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean v) {
        this.online = v;
    }
}