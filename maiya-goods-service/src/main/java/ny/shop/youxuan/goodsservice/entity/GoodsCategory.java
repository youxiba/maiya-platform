package ny.shop.youxuan.goodsservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("goods_category")
public class GoodsCategory {
    private Integer id;
    private String mid;
    private String categoryId;
    private String categoryName;
    private String parentId;
    private Integer level;
    private Integer sort;
    private Boolean staFlag;

    public Integer getId() { return id; }
    public void setId(Integer v) { this.id = v; }
    public String getMid() { return mid; }
    public void setMid(String v) { this.mid = v; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String v) { this.categoryId = v; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String v) { this.categoryName = v; }
    public String getParentId() { return parentId; }
    public void setParentId(String v) { this.parentId = v; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer v) { this.level = v; }
    public Integer getSort() { return sort; }
    public void setSort(Integer v) { this.sort = v; }
    public Boolean getStaFlag() { return staFlag; }
    public void setStaFlag(Boolean v) { this.staFlag = v; }
}
