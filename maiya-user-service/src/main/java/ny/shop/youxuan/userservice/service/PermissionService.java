package ny.shop.youxuan.userservice.service;

import com.alibaba.fastjson.JSON;
import ny.shop.youxuan.common.dto.UserPermissionDTO;
import ny.shop.youxuan.userservice.entity.UserInfo;
import ny.shop.youxuan.userservice.mapper.RightInfoMapper;
import ny.shop.youxuan.userservice.mapper.UserInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户权限服务
 *
 * 设计原则：
 * 1. 登录时写入缓存（由 AuthService 调用 refresh）
 * 2. 校验时只读缓存（由 Gateway 调用 getUserPermissions）
 * 3. 变更时主动失效（由 Admin 调用 clear/refresh）
 *
 * 缓存 Key：user:permissions:{uid}
 * 缓存 TTL：24 小时
 */
@Service
public class PermissionService {

    private static final Logger log = LoggerFactory.getLogger(PermissionService.class);

    private static final String PERM_CACHE_PREFIX = "user:permissions:";
    private static final long CACHE_EXPIRE_HOURS = 24;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RightInfoMapper rightInfoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // ========================================================================
    // 缓存读取
    // ========================================================================

    /**
     * 获取用户权限（优先从 Redis 读取）
     *
     * 调用方：Gateway JwtAuthGlobalFilter
     * 缓存未命中时自动从 DB 加载并回填缓存
     */
    public UserPermissionDTO getUserPermissions(String userId) {
        if (userId == null || userId.isEmpty()) {
            return emptyPermission(userId);
        }

        String cacheKey = PERM_CACHE_PREFIX + userId;

        // 1. 尝试从 Redis 读取
        String json = redisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.hasText(json)) {
            try {
                UserPermissionDTO dto = JSON.parseObject(json, UserPermissionDTO.class);
                log.debug("权限缓存命中: userId={}", userId);
                return dto;
            } catch (Exception e) {
                log.warn("权限缓存解析失败, 重新加载: userId={}", userId);
            }
        }

        // 2. 缓存未命中，从 DB 加载
        UserPermissionDTO dto = loadPermissionsFromDb(userId);

        // 3. 写入 Redis
        redisTemplate.opsForValue().set(
                cacheKey,
                JSON.toJSONString(dto),
                CACHE_EXPIRE_HOURS,
                TimeUnit.HOURS);

        log.info("权限已从 DB 加载并缓存: userId={}, roles={}, perms={}",
                userId, dto.getRoles().size(), dto.getPermissions().size());

        return dto;
    }

    // ========================================================================
    // 缓存管理
    // ========================================================================

    /**
     * 刷新缓存（用于权限变更后主动更新）
     *
     * 调用方：角色管理、权限分配完成后
     */
    public void refreshUserPermissions(String userId) {
        if (userId == null || userId.isEmpty()) return;

        String cacheKey = PERM_CACHE_PREFIX + userId;
        UserPermissionDTO dto = loadPermissionsFromDb(userId);

        redisTemplate.opsForValue().set(
                cacheKey,
                JSON.toJSONString(dto),
                CACHE_EXPIRE_HOURS,
                TimeUnit.HOURS);

        log.info("权限缓存已刷新: userId={}", userId);
    }

    /**
     * 清除缓存（用于用户登出或权限被删除）
     */
    public void clearUserPermissions(String userId) {
        if (userId == null || userId.isEmpty()) return;

        redisTemplate.delete(PERM_CACHE_PREFIX + userId);
        log.info("权限缓存已清除: userId={}", userId);
    }

    // ========================================================================
    // 权限校验
    // ========================================================================

    /**
     * 判断用户是否拥有指定角色
     */
    public boolean hasRole(String userId, String role) {
        UserPermissionDTO dto = getUserPermissions(userId);
        return dto.getRoles() != null && dto.getRoles().contains(role);
    }

    /**
     * 判断用户是否拥有指定权限
     */
    public boolean hasPermission(String userId, String permission) {
        UserPermissionDTO dto = getUserPermissions(userId);
        return dto.getPermissions() != null && dto.getPermissions().contains(permission);
    }

    /**
     * 判断用户是否拥有任意一个指定角色
     */
    public boolean hasAnyRole(String userId, String... roles) {
        UserPermissionDTO dto = getUserPermissions(userId);
        if (dto.getRoles() == null) return false;
        for (String role : roles) {
            if (dto.getRoles().contains(role)) return true;
        }
        return false;
    }

    /**
     * 判断用户是否为超级管理员
     */
    public boolean isSuperAdmin(String userId) {
        return hasRole(userId, "ROLE_SUPERADMIN");
    }

    // ========================================================================
    // 私有方法
    // ========================================================================

    /**
     * 从数据库加载权限数据
     *
     * 数据来源：
     * 1. user_info.rights 字段（逗号分隔的角色字符串）
     * 2. user_role + role_right_info + right_info 多表关联
     */
    private UserPermissionDTO loadPermissionsFromDb(String userId) {
        // 1. 查询用户基本信息
        UserInfo user = userInfoMapper.findByUid(userId);

        // 2. 从 user_info.rights 字段解析角色
        Set<String> roles = new HashSet<>();
        if (user != null && StringUtils.hasText(user.getRoles())) {
            String[] roleArray = user.getRoles().split(",");
            for (String r : roleArray) {
                String trimmed = r.trim();
                if (!trimmed.isEmpty()) {
                    roles.add(trimmed);
                }
            }
        }

        // 3. 从 user_role 关联表补充角色
        try {
            List<String> roleCodes = rightInfoMapper.findRoleCodesByUid(userId);
            roles.addAll(roleCodes);
        } catch (Exception e) {
            log.warn("关联表查询角色失败(可能无数据): userId={}", userId);
        }

        // 4. 通过角色链路查询权限标识
        Set<String> permissions = new HashSet<>();
        try {
            List<String> permCodes = rightInfoMapper.findPermCodesByUid(userId);
            permissions.addAll(permCodes);
        } catch (Exception e) {
            log.warn("关联表查询权限失败(可能无数据): userId={}", userId);
        }

        String username = user != null ? user.getUsername() : null;
        return new UserPermissionDTO(userId, username, roles, permissions);
    }

    /**
     * 返回空权限（用于用户不存在或参数为空）
     */
    private UserPermissionDTO emptyPermission(String userId) {
        return new UserPermissionDTO(userId, null,
                Collections.emptySet(), Collections.emptySet());
    }
}
