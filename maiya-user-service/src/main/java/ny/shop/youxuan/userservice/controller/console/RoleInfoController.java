package ny.shop.youxuan.userservice.controller.console;

import com.alibaba.fastjson.JSONObject;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.common.result.PageResult;
import ny.shop.youxuan.userservice.dto.RoleInfoDto;
import ny.shop.youxuan.userservice.entity.RoleInfo;
import ny.shop.youxuan.userservice.service.RoleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理 API — 角色管理
 *
 * 路径映射：/api/admin/user/role/**
 * 权限：需管理员角色（网关层校验）
 */
@RestController
@RequestMapping("/api/admin/user/role")
public class RoleInfoController {

    @Autowired
    private RoleInfoService roleInfoService;

    /** 添加角色 */
    @PostMapping("/add")
    public ApiResult<RoleInfo> addRole(@RequestBody RoleInfoDto roleInfoDto) {
        return roleInfoService.addRole(roleInfoDto);
    }

    /** 修改角色 */
    @PostMapping("/update")
    public ApiResult<RoleInfo> updateRole(@RequestBody RoleInfoDto roleInfoDto) {
        return roleInfoService.updateRole(roleInfoDto);
    }

    /** 删除角色 */
    @GetMapping("/del")
    public ApiResult<Boolean> deleteRole(@RequestParam String roleId) {
        return roleInfoService.deleteRole(roleId);
    }

    /** 查询角色 */
    @GetMapping("/getbyid")
    public ApiResult<RoleInfo> getByRoleId(@RequestParam String roleId) {
        return roleInfoService.getByRoleId(roleId);
    }

    /** 查询用户的角色列表 */
    @GetMapping("/getbyuid")
    public ApiResult<List<RoleInfo>> getByUid(@RequestParam String uid) {
        return roleInfoService.getByUid(uid);
    }

    /** 查询用户的角色列表（分页） */
    @GetMapping("/getbyuidpage")
    public ApiResult<PageResult<RoleInfo>> getByUidPage(
            @RequestParam String uid,
            @RequestParam int pageIndex,
            @RequestParam int pageSize) {
        return roleInfoService.getByUid(uid, pageIndex, pageSize);
    }

    /** 按角色名称搜索 */
    @GetMapping("/search")
    public ApiResult<List<RoleInfo>> searchByRoleName(
            @RequestParam String uid,
            @RequestParam String roleName) {
        return roleInfoService.getByUidAndRoleNameLike(uid, roleName);
    }

    /** 查询角色的权限列表 */
    @GetMapping("/rights")
    public ApiResult<List<JSONObject>> getRoleRights(@RequestParam String roleId) {
        return roleInfoService.getRoleRights(roleId);
    }
}
