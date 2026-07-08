package ny.shop.youxuan.notificationservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("message_info")
public class MessageInfo {
    private Integer id;
    private String infoId;
    private String uid;
    private Integer msgType;
    private String msgTitle;
    private String orderId;
    private Integer msgStatus;
    private Integer pushType;
    private String msgText;
    private String msgUrl;
    private String goodsUrl;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer v) {
        this.msgType = v;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String v) {
        this.msgTitle = v;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String v) {
        this.orderId = v;
    }

    public Integer getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Integer v) {
        this.msgStatus = v;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer v) {
        this.pushType = v;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String v) {
        this.msgText = v;
    }

    public String getMsgUrl() {
        return msgUrl;
    }

    public void setMsgUrl(String v) {
        this.msgUrl = v;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String v) {
        this.goodsUrl = v;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long v) {
        this.createTime = v;
    }
}