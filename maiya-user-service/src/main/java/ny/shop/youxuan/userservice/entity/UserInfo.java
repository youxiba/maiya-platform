package ny.shop.youxuan.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;

@TableName("user_info")
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String uid;
    private String username;
    private String password;
    private String creatorUid;
    private String mid;
    private String realname;
    private String nickname;
    private String avatar;
    private String appleId;
    private String qqid;
    private String unionId;
    private String openId;
    private String wxNickname;
    private String wxAvatar;
    private String telephone;
    private String inviteCode;
    private String superUid;
    private String superCode;
    private String bigGroupUid;
    private String partGroupUid;
    private Integer level;
    private String roles;
    private Long createTime;
    private Long regtime;
    private Long lastPasswordResetDate;
    private Boolean phoneBindingFlag;
    private Boolean appBindingFlag;
    private Boolean enable;
    private String regSource;
    private String addressId;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String v) {
        this.username = v;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String v) {
        this.password = v;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String v) {
        this.nickname = v;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String v) {
        this.telephone = v;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String v) {
        this.inviteCode = v;
    }

    public String getSuperUid() {
        return superUid;
    }

    public void setSuperUid(String v) {
        this.superUid = v;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String v) {
        this.unionId = v;
    }

    public String getQqid() {
        return qqid;
    }

    public void setQqid(String v) {
        this.qqid = v;
    }

    public String getAppleId() {
        return appleId;
    }

    public void setAppleId(String v) {
        this.appleId = v;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String v) {
        this.roles = v;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer v) {
        this.level = v;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean v) {
        this.enable = v;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long v) {
        this.createTime = v;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String v) {
        this.avatar = v;
    }
}