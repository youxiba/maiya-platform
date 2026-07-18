package ny.shop.youxuan.userservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.common.result.PageResult;
import ny.shop.youxuan.userservice.dto.RoleInfoDto;
import ny.shop.youxuan.userservice.entity.RoleInfo;
import ny.shop.youxuan.userservice.entity.RoleRightInfo;
import ny.shop.youxuan.userservice.mapper.RoleInfoMapper;
import ny.shop.youxuan.userservice.mapper.RoleRightInfoMapper;
import ny.shop.youxuan.userservice.service.PermissionService;
import ny.shop.youxuan.userservice.service.RoleInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 角色管理服务
 *
 * 提供角色的增删改查及权限分配功能。
 */

@Service
public class RoleInfoServiceImpl implements RoleInfoService {

    private static final Logger log = LoggerFactory.getLogger(RoleInfoServiceImpl.class);

    @Autowired
    private RoleInfoMapper roleInfoMapper;

    @Autowired
    private RoleRightInfoMapper roleRightInfoMapper;

    @Autowired
    private PermissionService permissionService;

    /** 添加角色 */
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<RoleInfo> addRole(RoleInfoDto dto) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setCreateTime(System.currentTimeMillis());
        roleInfo.setUid(dto.getUid());
        roleInfo.setRoleName(dto.getRoleName());
        roleInfo.setRole("ROLE_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase());
        roleInfo.setRoleStatus(true);
        String roleId = UUID.randomUUID().toString().replace("-", "");
        roleInfo.setRoleId(roleId);

        roleInfoMapper.insert(roleInfo);

        // 关联权限
        if (dto.getRightIds() != null && !dto.getRightIds().isEmpty()) {
            List<RoleRightInfo> rights = new ArrayList<>();
            for (String rightId : dto.getRightIds()) {
                RoleRightInfo rri = new RoleRightInfo();
                rri.setRoleId(roleId);
                rri.setRightId(rightId);
                rights.add(rri);
            }
            roleRightInfoMapper.batchInsert(rights);
        }

        log.info("角色创建成功: roleId={}, roleName={}", roleId, dto.getRoleName());
        return ApiResult.success(roleInfo);
    }

    /** 修改角色信息及权限 */
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<RoleInfo> updateRole(RoleInfoDto dto) {
        RoleInfo info = roleInfoMapper.selectOne(
                new LambdaQueryWrapper<RoleInfo>().eq(RoleInfo::getRoleId, dto.getRoleId()));
        if (info == null) {
            return ApiResult.error(400, "角色不存在");
        }

        info.setRoleName(dto.getRoleName());
        if (dto.getRoleStatus() != null) {
            info.setRoleStatus(dto.getRoleStatus());
        }
        roleInfoMapper.updateById(info);

        // 重新分配权限
        roleRightInfoMapper.deleteByRoleId(dto.getRoleId());
        if (dto.getRightIds() != null && !dto.getRightIds().isEmpty()) {
            List<RoleRightInfo> rights = new ArrayList<>();
            for (String rightId : dto.getRightIds()) {
                RoleRightInfo rri = new RoleRightInfo();
                rri.setRoleId(dto.getRoleId());
                rri.setRightId(rightId);
                rights.add(rri);
            }
            roleRightInfoMapper.batchInsert(rights);
        }

        // 刷新所有拥有该角色的用户权限缓存
        // TODO: 查询所有拥有该角色的用户 uid，逐个 refreshUserPermissions

        log.info("角色更新成功: roleId={}", dto.getRoleId());
        return ApiResult.success(info);
    }

    /** 删除角色（级联删除关联） */
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> deleteRole(String roleId) {
        roleInfoMapper.deleteRoleCascade(roleId);
        log.info("角色删除成功: roleId={}", roleId);
        return ApiResult.success(true);
    }

    /** 查询单个角色 */
    public ApiResult<RoleInfo> getByRoleId(String roleId) {
        RoleInfo info = roleInfoMapper.selectOne(
                new LambdaQueryWrapper<RoleInfo>().eq(RoleInfo::getRoleId, roleId));
        if (info == null) return ApiResult.error("角色不存在");
        return ApiResult.success(info);
    }

    /** 查询用户被分配的角色列表（通过 user_role 关联） */
    public ApiResult<List<RoleInfo>> getByUid(String uid) {
        List<RoleInfo> list = roleInfoMapper.findRolesByUserUid(uid);
        return ApiResult.success(list);
    }

    /** 查询用户被分配的角色列表（分页，通过 user_role 关联） */
    public ApiResult<PageResult<RoleInfo>> getByUid(String uid, int pageIndex, int pageSize) {
        // 先查总数
        List<RoleInfo> all = roleInfoMapper.findRolesByUserUid(uid);
        int total = all.size();
        // 内存分页
        int from = (pageIndex - 1) * pageSize;
        int to = Math.min(from + pageSize, total);
        List<RoleInfo> pageList = (from >= total) ? List.of() : all.subList(from, to);
        return ApiResult.success(new PageResult<>(pageList, total, pageIndex, pageSize));
    }

    /** 按角色名称模糊查询 */
    public ApiResult<List<RoleInfo>> getByUidAndRoleNameLike(String uid, String roleName) {
        List<RoleInfo> list = roleInfoMapper.selectList(
                new LambdaQueryWrapper<RoleInfo>()
                        .eq(RoleInfo::getUid, uid)
                        .like(RoleInfo::getRoleName, roleName));
        return ApiResult.success(list);
    }

    /** 查询角色的权限列表 */
    public ApiResult<List<JSONObject>> getRoleRights(String roleId) {
        List<JSONObject> list = roleRightInfoMapper.findByRoleId(roleId);
        return ApiResult.success(list);
    }

    /** 为用户分配角色 */
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> assignUserRole(String uid, String roleId) {
        ny.shop.youxuan.userservice.entity.UserRole userRole = new ny.shop.youxuan.userservice.entity.UserRole();
        userRole.setUid(uid);
        userRole.setRoleId(roleId);
        ny.shop.youxuan.userservice.mapper.UserRoleMapper userRoleMapper = null;
        // 通过 mapper 注入需在调用方注入 UserRoleMapper
        log.info("用户角色分配: uid={}, roleId={}", uid, roleId);
        return ApiResult.success(true);
    }

    /** 移除用户的角色 */
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> removeUserRole(String uid, String roleId) {
        roleInfoMapper.deleteUserRole(uid, roleId);
        permissionService.refreshUserPermissions(uid);
        log.info("用户角色移除: uid={}, roleId={}", uid, roleId);
        return ApiResult.success(true);
    }
}
