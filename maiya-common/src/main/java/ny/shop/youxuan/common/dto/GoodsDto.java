package ny.shop.youxuan.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class GoodsDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String goodsInfoId;
    private String goodsId;
    private String goodsName;
    private String picsUrl;
    private BigDecimal goodsPrice;
    private BigDecimal activePrice;
    private Integer sum;
    private Integer sales;

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
}