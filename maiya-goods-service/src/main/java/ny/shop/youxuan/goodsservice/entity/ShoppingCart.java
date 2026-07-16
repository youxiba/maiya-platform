package ny.shop.youxuan.goodsservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("shopping_cart")
public class ShoppingCart {
    private Integer id;
    private String uid;
    private String mid;
    private String goodsInfoId;
    private String goodsName;
    private String goodsPic;
    private BigDecimal goodsPrice;
    private Integer goodsNum;
    private Boolean checked;
    private Long createTime;
    private Long updateTime;

    public Integer getId() { return id; }
    public void setId(Integer v) { this.id = v; }
    public String getUid() { return uid; }
    public void setUid(String v) { this.uid = v; }
    public String getMid() { return mid; }
    public void setMid(String v) { this.mid = v; }
    public String getGoodsInfoId() { return goodsInfoId; }
    public void setGoodsInfoId(String v) { this.goodsInfoId = v; }
    public String getGoodsName() { return goodsName; }
    public void setGoodsName(String v) { this.goodsName = v; }
    public String getGoodsPic() { return goodsPic; }
    public void setGoodsPic(String v) { this.goodsPic = v; }
    public BigDecimal getGoodsPrice() { return goodsPrice; }
    public void setGoodsPrice(BigDecimal v) { this.goodsPrice = v; }
    public Integer getGoodsNum() { return goodsNum; }
    public void setGoodsNum(Integer v) { this.goodsNum = v; }
    public Boolean getChecked() { return checked; }
    public void setChecked(Boolean v) { this.checked = v; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long v) { this.createTime = v; }
    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long v) { this.updateTime = v; }
}
