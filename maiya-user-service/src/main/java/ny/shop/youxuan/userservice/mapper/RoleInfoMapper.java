package ny.shop.youxuan.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.userservice.entity.RoleInfo;

import java.util.List;

@Mapper
public interface RoleInfoMapper extends BaseMapper<RoleInfo> {

    /** 通过 role 标识查询 */
    @Select("SELECT * FROM role_info WHERE role = #{role} LIMIT 1")
    RoleInfo findByRole(@Param("role") String role);

    /** 删除角色及其关联（删除角色 + 角色权限 + 用户角色） */
    @Delete("DELETE ri, rri, ur FROM role_info ri " +
            "LEFT JOIN role_right_info rri ON ri.role_id = rri.role_id " +
            "LEFT JOIN user_role ur ON ri.role_id = ur.role_id " +
            "WHERE ri.role_id = #{roleId}")
    int deleteRoleCascade(@Param("roleId") String roleId);

    /** 删除用户的所有角色 */
    @Delete("DELETE FROM user_role WHERE uid = #{uid}")
    int deleteUserRoles(@Param("uid") String uid);

    /** 删除用户的指定角色 */
    @Delete("DELETE FROM user_role WHERE uid = #{uid} AND role_id = #{roleId}")
    int deleteUserRole(@Param("uid") String uid, @Param("roleId") String roleId);
}
