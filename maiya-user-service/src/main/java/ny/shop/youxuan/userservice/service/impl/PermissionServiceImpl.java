package ny.shop.youxuan.userservice.service.impl;

import com.alibaba.fastjson.JSON;
import ny.shop.youxuan.common.dto.UserPermissionDTO;
import ny.shop.youxuan.userservice.entity.UserInfo;
import ny.shop.youxuan.userservice.mapper.RightInfoMapper;
import ny.shop.youxuan.userservice.mapper.UserInfoMapper;
import ny.shop.youxuan.userservice.service.PermissionService;
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
 * 用户权限服务实现（Redis 缓存版）
 *
 * <p>设计原则：
 * <ol>
 *   <li>登录时写入缓存（由 AuthService 调用 refresh）</li>
 *   <li>校验时只读缓存（由 Gateway 调用 getUserPermissions）</li>
 *   <li>变更时主动失效（由 Admin 调用 clear/refresh）</li>
 * </ol>
 *
 * <p>缓存 Key：user:permissions:{uid}
 * <br>缓存 TTL：24 小时
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private static final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

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

    @Override
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

    @Override
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

    @Override
    public void clearUserPermissions(String userId) {
        if (userId == null || userId.isEmpty()) return;

        redisTemplate.delete(PERM_CACHE_PREFIX + userId);
        log.info("权限缓存已清除: userId={}", userId);
    }

    // ========================================================================
    // 权限校验
    // ========================================================================

    @Override
    public boolean hasRole(String userId, String role) {
        UserPermissionDTO dto = getUserPermissions(userId);
        return dto.getRoles() != null && dto.getRoles().contains(role);
    }

    @Override
    public boolean hasPermission(String userId, String permission) {
        UserPermissionDTO dto = getUserPermissions(userId);
        return dto.getPermissions() != null && dto.getPermissions().contains(permission);
    }

    @Override
    public boolean hasAnyRole(String userId, String... roles) {
        UserPermissionDTO dto = getUserPermissions(userId);
        if (dto.getRoles() == null) return false;
        for (String role : roles) {
            if (dto.getRoles().contains(role)) return true;
        }
        return false;
    }

    @Override
    public boolean isSuperAdmin(String userId) {
        return hasRole(userId, "ROLE_SUPERADMIN");
    }

    // ========================================================================
    // 私有方法
    // ========================================================================

    private UserPermissionDTO loadPermissionsFromDb(String userId) {
        UserInfo user = userInfoMapper.findByUid(userId);

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

        try {
            List<String> roleCodes = rightInfoMapper.findRoleCodesByUid(userId);
            roles.addAll(roleCodes);
        } catch (Exception e) {
            log.warn("关联表查询角色失败(可能无数据): userId={}", userId);
        }

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

    private UserPermissionDTO emptyPermission(String userId) {
        return new UserPermissionDTO(userId, null,
                Collections.emptySet(), Collections.emptySet());
    }
}
