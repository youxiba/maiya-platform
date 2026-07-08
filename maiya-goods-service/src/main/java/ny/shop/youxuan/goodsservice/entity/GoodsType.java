package ny.shop.youxuan.goodsservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("goods_type")
public class GoodsType {
    private Integer id;
    private String typeId;
    private String categoryId;
    private String typeName;
    private Integer sort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String v) {
        this.typeId = v;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String v) {
        this.categoryId = v;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String v) {
        this.typeName = v;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer v) {
        this.sort = v;
    }
}