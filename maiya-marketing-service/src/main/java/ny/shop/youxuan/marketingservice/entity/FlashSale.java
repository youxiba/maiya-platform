package ny.shop.youxuan.marketingservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("flash_sale")
public class FlashSale {
    private Integer id;
    private String fsId;
    private String fsName;
    private String fsDesc;
    private Integer fsMchs;
    private Integer mchs;
    private Integer fsQty;
    private Long startDate;
    private Long endDate;
    private Long startTime;
    private Long endTime;
    private Boolean enable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getFsId() {
        return fsId;
    }

    public void setFsId(String v) {
        this.fsId = v;
    }

    public String getFsName() {
        return fsName;
    }

    public void setFsName(String v) {
        this.fsName = v;
    }

    public String getFsDesc() {
        return fsDesc;
    }

    public void setFsDesc(String v) {
        this.fsDesc = v;
    }

    public Integer getFsMchs() {
        return fsMchs;
    }

    public void setFsMchs(Integer v) {
        this.fsMchs = v;
    }

    public Integer getMchs() {
        return mchs;
    }

    public void setMchs(Integer v) {
        this.mchs = v;
    }

    public Integer getFsQty() {
        return fsQty;
    }

    public void setFsQty(Integer v) {
        this.fsQty = v;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long v) {
        this.startDate = v;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long v) {
        this.endDate = v;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long v) {
        this.startTime = v;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long v) {
        this.endTime = v;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean v) {
        this.enable = v;
    }
}