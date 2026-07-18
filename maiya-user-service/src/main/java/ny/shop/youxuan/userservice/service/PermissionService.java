package ny.shop.youxuan.userservice.service;

import ny.shop.youxuan.common.dto.UserPermissionDTO;

/**
 * 用户权限服务接口
 *
 * <p>设计原则：
 * <ol>
 *   <li>登录时写入缓存（由 AuthService 调用 refresh）</li>
 *   <li>校验时只读缓存（由 Gateway 调用 getUserPermissions）</li>
 *   <li>变更时主动失效（由 Admin 调用 clear/refresh）</li>
 * </ol>
 *
 * <p>接口化目的：支持多种缓存实现切换（Redis / Caffeine / 数据库直读），
 * 方便单元测试 Mock。
 */
public interface PermissionService {

    /**
     * 获取用户权限（优先从缓存读取，未命中则从 DB 加载并回填缓存）
     *
     * @param userId 用户 UID
     * @return 用户权限 DTO（含角色和权限标识列表）
     */
    UserPermissionDTO getUserPermissions(String userId);

    /**
     * 刷新缓存（用于权限变更后主动更新）
     *
     * @param userId 用户 UID
     */
    void refreshUserPermissions(String userId);

    /**
     * 清除缓存（用于用户登出或权限被删除）
     *
     * @param userId 用户 UID
     */
    void clearUserPermissions(String userId);

    /**
     * 判断用户是否拥有指定角色
     */
    boolean hasRole(String userId, String role);

    /**
     * 判断用户是否拥有指定权限
     */
    boolean hasPermission(String userId, String permission);

    /**
     * 判断用户是否拥有任意一个指定角色
     */
    boolean hasAnyRole(String userId, String... roles);

    /**
     * 判断用户是否为超级管理员
     */
    boolean isSuperAdmin(String userId);
}
