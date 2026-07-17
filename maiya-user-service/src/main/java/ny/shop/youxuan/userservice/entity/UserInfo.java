package ny.shop.youxuan.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

import java.io.Serializable;
@Data
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

}