package ny.shop.youxuan.goodsservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;

@TableName("goods_info")
public class GoodsInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String mid;
    private String goodsInfoId;
    private String goodsId;
    private String goodsName;
    private String picsUrl;
    private String goodsDesc;
    private String storeName;
    private String categoryId;
    private String goodsTypeId;
    private BigDecimal marketPrice;
    private BigDecimal goodsPrice;
    private BigDecimal activePrice;
    private BigDecimal goodsCost;
    private Integer sum;
    private Integer sales;
    private Double score;
    private Boolean staFlag;
    private String activeId;
    private Integer activeType;
    private Integer activeStatus;
    private Integer points;
    private Boolean specFlag;
    private Boolean attriFlag;
    private Boolean packagingFlag;
    private BigDecimal packagingFee;
    private Boolean recommendFlag;
    private Boolean startSendFlag;

    public String getMid() {
        return mid;
    }

    public void setMid(String v) {
        this.mid = v;
    }

    public String getGoodsInfoId() {
        return goodsInfoId;
    }

    public void setGoodsInfoId(String v) {
        this.goodsInfoId = v;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String v) {
        this.goodsId = v;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String v) {
        this.goodsName = v;
    }

    public String getPicsUrl() {
        return picsUrl;
    }

    public void setPicsUrl(String v) {
        this.picsUrl = v;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal v) {
        this.goodsPrice = v;
    }

    public BigDecimal getActivePrice() {
        return activePrice;
    }

    public void setActivePrice(BigDecimal v) {
        this.activePrice = v;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal v) {
        this.marketPrice = v;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer v) {
        this.sum = v;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer v) {
        this.sales = v;
    }

    public Boolean getStaFlag() {
        return staFlag;
    }

    public void setStaFlag(Boolean v) {
        this.staFlag = v;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String v) {
        this.categoryId = v;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String v) {
        this.goodsTypeId = v;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String v) {
        this.storeName = v;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer v) {
        this.points = v;
    }
}