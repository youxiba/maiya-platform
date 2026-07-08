package ny.shop.youxuan.common.dto;

import com.alibaba.fastjson.JSONObject;
import ny.shop.youxuan.common.enums.DtbtType;
import java.io.Serializable;

public class OrderAccountDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String uid;
    private String mid;
    private String mchUid;
    private String superUid;
    private String orderId;
    private String ywUid;
    private String zjUid;
    private String goodsName;
    private String goodsUrl;
    private DtbtType dtbtType;
    private JSONObject params;

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

    public String getMchUid() {
        return mchUid;
    }

    public void setMchUid(String v) {
        this.mchUid = v;
    }

    public String getSuperUid() {
        return superUid;
    }

    public void setSuperUid(String v) {
        this.superUid = v;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String v) {
        this.orderId = v;
    }

    public String getYwUid() {
        return ywUid;
    }

    public void setYwUid(String v) {
        this.ywUid = v;
    }

    public String getZjUid() {
        return zjUid;
    }

    public void setZjUid(String v) {
        this.zjUid = v;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String v) {
        this.goodsName = v;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String v) {
        this.goodsUrl = v;
    }

    public DtbtType getDtbtType() {
        return dtbtType;
    }

    public void setDtbtType(DtbtType v) {
        this.dtbtType = v;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject v) {
        this.params = v;
    }
}