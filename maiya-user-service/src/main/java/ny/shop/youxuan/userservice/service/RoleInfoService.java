package ny.shop.youxuan.userservice.service;

import com.alibaba.fastjson.JSONObject;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.common.result.PageResult;
import ny.shop.youxuan.userservice.dto.RoleInfoDto;
import ny.shop.youxuan.userservice.entity.RoleInfo;

import java.util.List;

/**
 * 角色管理服务接口
 */
public interface RoleInfoService {

    ApiResult<RoleInfo> addRole(RoleInfoDto dto);

    ApiResult<RoleInfo> updateRole(RoleInfoDto dto);

    ApiResult<Boolean> deleteRole(String roleId);

    ApiResult<RoleInfo> getByRoleId(String roleId);

    ApiResult<List<RoleInfo>> getByUid(String uid);

    ApiResult<PageResult<RoleInfo>> getByUid(String uid, int pageIndex, int pageSize);

    ApiResult<List<RoleInfo>> getByUidAndRoleNameLike(String uid, String roleName);

    ApiResult<List<JSONObject>> getRoleRights(String roleId);

    ApiResult<Boolean> assignUserRole(String uid, String roleId);

    ApiResult<Boolean> removeUserRole(String uid, String roleId);
}
