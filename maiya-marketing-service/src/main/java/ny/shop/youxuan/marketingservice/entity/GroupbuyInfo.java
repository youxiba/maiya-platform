package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("groupbuy_info")
public class GroupbuyInfo {
    private Integer id;
    private String groupId;
    private String storeId;
    private String goodsInfoId;
    private Integer groupSum;
    private Integer groupLim;
    private Long endTime;
    private Integer groupType;
    private Integer groupStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String v) {
        this.groupId = v;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String v) {
        this.storeId = v;
    }

    public String getGoodsInfoId() {
        return goodsInfoId;
    }

    public void setGoodsInfoId(String v) {
        this.goodsInfoId = v;
    }

    public Integer getGroupSum() {
        return groupSum;
    }

    public void setGroupSum(Integer v) {
        this.groupSum = v;
    }

    public Integer getGroupLim() {
        return groupLim;
    }

    public void setGroupLim(Integer v) {
        this.groupLim = v;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long v) {
        this.endTime = v;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer v) {
        this.groupType = v;
    }

    public Integer getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(Integer v) {
        this.groupStatus = v;
    }
}