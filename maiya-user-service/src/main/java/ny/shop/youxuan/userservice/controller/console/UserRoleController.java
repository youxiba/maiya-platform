package ny.shop.youxuan.userservice.controller.console;

import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.userservice.entity.UserRole;
import ny.shop.youxuan.userservice.mapper.UserRoleMapper;
import ny.shop.youxuan.userservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理 API — 用户角色分配
 *
 * 路径映射：/api/admin/user/role/assign/**
 */
@RestController
@RequestMapping("/api/admin/user/role/assign")
public class UserRoleController {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PermissionService permissionService;

    /** 查询用户的角色 */
    @GetMapping("/list")
    public ApiResult<List<UserRole>> getUserRoles(@RequestParam String uid) {
        return ApiResult.success(userRoleMapper.findByUid(uid));
    }

    /** 为用户分配角色 */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> assignRole(@RequestParam String uid, @RequestParam String roleId) {
        UserRole userRole = new UserRole();
        userRole.setUid(uid);
        userRole.setRoleId(roleId);
        userRoleMapper.insert(userRole);
        permissionService.refreshUserPermissions(uid);
        return ApiResult.success(true);
    }

    /** 移除用户的角色 */
    @GetMapping("/remove")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> removeRole(@RequestParam String uid, @RequestParam String roleId) {
        userRoleMapper.deleteByUid(uid);
        permissionService.refreshUserPermissions(uid);
        return ApiResult.success(true);
    }

    /** 批量设置用户角色（先清后加） */
    @PostMapping("/batch")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> batchAssignRoles(@RequestParam String uid, @RequestBody List<String> roleIds) {
        userRoleMapper.deleteByUid(uid);
        for (String roleId : roleIds) {
            UserRole ur = new UserRole();
            ur.setUid(uid);
            ur.setRoleId(roleId);
            userRoleMapper.insert(ur);
        }
        permissionService.refreshUserPermissions(uid);
        return ApiResult.success(true);
    }
}
