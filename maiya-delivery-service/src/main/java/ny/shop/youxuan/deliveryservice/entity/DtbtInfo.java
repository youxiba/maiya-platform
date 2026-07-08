package ny.shop.youxuan.deliveryservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("dtbt_info")
public class DtbtInfo {
    private Integer id;
    private String infoId;
    private String orderId;
    private String subId;
    private String uid;
    private String mid;
    private String rid;
    private String stationId;
    private Integer dtbtOrderType;
    private Integer dtbtStatus;
    private BigDecimal dtbtFee;
    private String storeName;
    private String storeAddress;
    private String storePhone;
    private Double storeLon;
    private Double storeLat;
    private String userName;
    private String userAddress;
    private String userPhone;
    private Double userLon;
    private Double userLat;
    private Long sendTime;
    private Long sendendTime;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String v) {
        this.orderId = v;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String v) {
        this.subId = v;
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

    public String getRid() {
        return rid;
    }

    public void setRid(String v) {
        this.rid = v;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String v) {
        this.stationId = v;
    }

    public Integer getDtbtOrderType() {
        return dtbtOrderType;
    }

    public void setDtbtOrderType(Integer v) {
        this.dtbtOrderType = v;
    }

    public Integer getDtbtStatus() {
        return dtbtStatus;
    }

    public void setDtbtStatus(Integer v) {
        this.dtbtStatus = v;
    }

    public BigDecimal getDtbtFee() {
        return dtbtFee;
    }

    public void setDtbtFee(BigDecimal v) {
        this.dtbtFee = v;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String v) {
        this.storeName = v;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String v) {
        this.storeAddress = v;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String v) {
        this.storePhone = v;
    }

    public Double getStoreLon() {
        return storeLon;
    }

    public void setStoreLon(Double v) {
        this.storeLon = v;
    }

    public Double getStoreLat() {
        return storeLat;
    }

    public void setStoreLat(Double v) {
        this.storeLat = v;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String v) {
        this.userName = v;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String v) {
        this.userAddress = v;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String v) {
        this.userPhone = v;
    }

    public Double getUserLon() {
        return userLon;
    }

    public void setUserLon(Double v) {
        this.userLon = v;
    }

    public Double getUserLat() {
        return userLat;
    }

    public void setUserLat(Double v) {
        this.userLat = v;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long v) {
        this.sendTime = v;
    }

    public Long getSendendTime() {
        return sendendTime;
    }

    public void setSendendTime(Long v) {
        this.sendendTime = v;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long v) {
        this.createTime = v;
    }
}