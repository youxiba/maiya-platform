package ny.shop.youxuan.financeservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("cefts_info")
public class CeftsInfo {
    private Integer id;
    private String infoId;
    private Integer auditStatus;
    private String uid;
    private String telephone;
    private Integer walletType;
    private BigDecimal fee;
    private Long accTime;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String v) {
        this.infoId = v;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer v) {
        this.auditStatus = v;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String v) {
        this.telephone = v;
    }

    public Integer getWalletType() {
        return walletType;
    }

    public void setWalletType(Integer v) {
        this.walletType = v;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal v) {
        this.fee = v;
    }

    public Long getAccTime() {
        return accTime;
    }

    public void setAccTime(Long v) {
        this.accTime = v;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String v) {
        this.remark = v;
    }
}