package ny.shop.youxuan.financeservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("account_info")
public class AccountInfo {
    private Integer id;
    private Long createTime;
    private Long billTime;
    private String serialNum;
    private String orderId;
    private String subId;
    private String goodsName;
    private String goodsUrl;
    private String uid;
    private String mid;
    private String traderUid;
    private String remark;
    private Integer balType;
    private Integer fundSource;
    private Integer fundFlow;
    private Integer accountType;
    private BigDecimal money;
    private BigDecimal balance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long v) {
        this.createTime = v;
    }

    public Long getBillTime() {
        return billTime;
    }

    public void setBillTime(Long v) {
        this.billTime = v;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String v) {
        this.serialNum = v;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String v) {
        this.orderId = v;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String v) {
        this.subId = v;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String v) {
        this.goodsName = v;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String v) {
        this.goodsUrl = v;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String v) {
        this.mid = v;
    }

    public String getTraderUid() {
        return traderUid;
    }

    public void setTraderUid(String v) {
        this.traderUid = v;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String v) {
        this.remark = v;
    }

    public Integer getBalType() {
        return balType;
    }

    public void setBalType(Integer v) {
        this.balType = v;
    }

    public Integer getFundSource() {
        return fundSource;
    }

    public void setFundSource(Integer v) {
        this.fundSource = v;
    }

    public Integer getFundFlow() {
        return fundFlow;
    }

    public void setFundFlow(Integer v) {
        this.fundFlow = v;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer v) {
        this.accountType = v;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal v) {
        this.money = v;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal v) {
        this.balance = v;
    }
}