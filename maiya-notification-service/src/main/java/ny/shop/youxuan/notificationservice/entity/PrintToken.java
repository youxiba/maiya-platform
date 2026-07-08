package ny.shop.youxuan.notificationservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("print_token")
public class PrintToken {
    private Integer id;
    private Long setTime;
    private String token;
    private String refreshToken;

    public Integer getId() {
        return id;
    }

    public void setId(Integer v) {
        this.id = v;
    }

    public Long getSetTime() {
        return setTime;
    }

    public void setSetTime(Long v) {
        this.setTime = v;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String v) {
        this.token = v;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String v) {
        this.refreshToken = v;
    }
}