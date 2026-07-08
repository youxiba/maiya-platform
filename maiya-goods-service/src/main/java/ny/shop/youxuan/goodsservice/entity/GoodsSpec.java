package ny.shop.youxuan.goodsservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("goods_spec")
public class GoodsSpec {
    private Integer id;
    private String specId;
    private String goodsInfoId;
    private String specName;
    private Integer sort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String v) {
        this.specId = v;
    }

    public String getGoodsInfoId() {
        return goodsInfoId;
    }

    public void setGoodsInfoId(String v) {
        this.goodsInfoId = v;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String v) {
        this.specName = v;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer v) {
        this.sort = v;
    }
}