package ny.shop.youxuan.userservice.vo;

import lombok.Data;

/**
 * 用户信息 VO（app/小程序端）
 *
 * 只暴露前端需要的安全字段，隔离 Entity 中的敏感数据。
 * 禁止返回：password、openId、unionId、内部 ID 等。
 */
@Data
public class UserInfoVO {

    private String uid;
    private String username;
    private String nickname;
    private String avatar;
    private String telephone;
    private String inviteCode;
    private Integer level;
    private String roles;
    private Long createTime;
    private Boolean enable;

   
}
