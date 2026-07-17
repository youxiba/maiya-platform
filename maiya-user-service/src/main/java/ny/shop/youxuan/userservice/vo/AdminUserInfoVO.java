package ny.shop.youxuan.userservice.vo;

import lombok.Data;

/**
 * 用户信息 VO（后台管理端）
 *
 * 比 app 端暴露更多字段（如 realname、wxNickname 等），
 * 但依然排除 password、openId、unionId 等敏感信息。
 */
@Data
public class AdminUserInfoVO {

    private String uid;
    private String username;
    private String nickname;
    private String realname;
    private String avatar;
    private String wxNickname;
    private String wxAvatar;
    private String telephone;
    private String inviteCode;
    private String superUid;
    private String creatorUid;
    private String mid;
    private Integer level;
    private String roles;
    private Long createTime;
    private Long regtime;
    private Boolean enable;

   
}
