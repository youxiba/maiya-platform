package ny.shop.youxuan.financeservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

@TableName("wallet_info")
public class WalletInfo {
    private Integer id;
    private String uid;
    private String password;
    private BigDecimal money;
    private BigDecimal mchMoney;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String v) {
        this.password = v;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal v) {
        this.money = v;
    }

    public BigDecimal getMchMoney() {
        return mchMoney;
    }

    public void setMchMoney(BigDecimal v) {
        this.mchMoney = v;
    }
}