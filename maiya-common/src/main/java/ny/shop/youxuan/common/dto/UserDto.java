package ny.shop.youxuan.common.dto;

import java.io.Serializable;

public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String uid;
    private String username;
    private String nickname;
    private String avatar;
    private String telephone;
    private String superUid;
    private String inviteCode;
    private Integer level;

    public String getUid() {
        return uid;
    }

    public void setUid(String v) {
        this.uid = v;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String v) {
        this.username = v;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String v) {
        this.nickname = v;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String v) {
        this.avatar = v;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String v) {
        this.telephone = v;
    }

    public String getSuperUid() {
        return superUid;
    }

    public void setSuperUid(String v) {
        this.superUid = v;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String v) {
        this.inviteCode = v;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer v) {
        this.level = v;
    }
}