package ny.shop.youxuan.userservice.vo;

import lombok.Data;

import java.util.Set;

/**
 * 登录响应 VO
 * <p>
 * 登录成功后返回给前端的数据，包含 JWT Token、用户基本信息、角色和权限列表。
 * 严禁返回 password、openId、unionId 等敏感字段。
 */
@Data
public class LoginVO {

    /** JWT Token（前端需写入 localStorage，请求时放在 Authorization 头） */
    private String token;

    /** 用户 UID */
    private String uid;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 头像 */
    private String avatar;

    /** 角色列表 */
    private Set<String> roles;

    /** 权限标识列表 */
    private Set<String> permissions;
}
