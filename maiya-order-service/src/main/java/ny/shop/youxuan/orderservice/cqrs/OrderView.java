package ny.shop.youxuan.orderservice.cqrs;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("order_view")
public class OrderView {
    private Long id;
    private String orderId;
    private String uid;
    private String nickname;
    private String storeName;
    private String goodsName;
    private String goodsUrl;
    private BigDecimal ahsrSum;
    private Integer orderStatus;
    private Long createTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String v) {
        this.orderId = v;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String v) {
        this.nickname = v;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String v) {
        this.storeName = v;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String v) {
        this.goodsName = v;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer v) {
        this.orderStatus = v;
    }
}