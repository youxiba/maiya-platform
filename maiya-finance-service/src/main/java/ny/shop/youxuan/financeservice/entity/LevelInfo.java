package ny.shop.youxuan.financeservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("level_info")
public class LevelInfo {
    private Integer id;
    private Integer ztsum;
    private Integer jtsum;
    private BigDecimal income;
    private BigDecimal consum;
    private Integer mgrsum;
    private BigDecimal mgincome;
    private BigDecimal mgconsum;
    private Integer myRatio;
    private Integer mchRatio;
    private Integer reRatio;
    private Integer ztRatio;
    private Integer yjRatio;
    private Integer ywRatio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public Integer getZtsum() {
        return ztsum;
    }

    public void setZtsum(Integer v) {
        this.ztsum = v;
    }

    public Integer getJtsum() {
        return jtsum;
    }

    public void setJtsum(Integer v) {
        this.jtsum = v;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal v) {
        this.income = v;
    }

    public BigDecimal getConsum() {
        return consum;
    }

    public void setConsum(BigDecimal v) {
        this.consum = v;
    }

    public Integer getMgrsum() {
        return mgrsum;
    }

    public void setMgrsum(Integer v) {
        this.mgrsum = v;
    }

    public BigDecimal getMgincome() {
        return mgincome;
    }

    public void setMgincome(BigDecimal v) {
        this.mgincome = v;
    }

    public BigDecimal getMgconsum() {
        return mgconsum;
    }

    public void setMgconsum(BigDecimal v) {
        this.mgconsum = v;
    }

    public Integer getMyRatio() {
        return myRatio;
    }

    public void setMyRatio(Integer v) {
        this.myRatio = v;
    }

    public Integer getMchRatio() {
        return mchRatio;
    }

    public void setMchRatio(Integer v) {
        this.mchRatio = v;
    }

    public Integer getReRatio() {
        return reRatio;
    }

    public void setReRatio(Integer v) {
        this.reRatio = v;
    }

    public Integer getZtRatio() {
        return ztRatio;
    }

    public void setZtRatio(Integer v) {
        this.ztRatio = v;
    }

    public Integer getYjRatio() {
        return yjRatio;
    }

    public void setYjRatio(Integer v) {
        this.yjRatio = v;
    }

    public Integer getYwRatio() {
        return ywRatio;
    }

    public void setYwRatio(Integer v) {
        this.ywRatio = v;
    }
}