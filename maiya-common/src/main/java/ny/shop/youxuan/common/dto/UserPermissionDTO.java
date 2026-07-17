package ny.shop.youxuan.common.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * 用户权限 DTO
 *
 * 用于在微服务间传递和 Redis 缓存用户权限数据。
 * 轻量级，仅包含鉴权所需的角色和权限标识。
 */
public class UserPermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private String username;
    private Set<String> roles;
    private Set<String> permissions;

    public UserPermissionDTO() {}

    public UserPermissionDTO(String userId, String username, Set<String> roles, Set<String> permissions) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.permissions = permissions;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }

    public Set<String> getPermissions() { return permissions; }
    public void setPermissions(Set<String> permissions) { this.permissions = permissions; }
}
