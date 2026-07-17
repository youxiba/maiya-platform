package ny.shop.youxuan.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.userservice.entity.RightInfo;

import java.util.List;

@Mapper
public interface RightInfoMapper extends BaseMapper<RightInfo> {

    /** 通过权限标识查询 rightId */
    @Select("SELECT right_id FROM right_info WHERE role_right = #{roleRight} LIMIT 1")
    String findRightIdByCode(@Param("roleRight") String roleRight);

    /** 获取用户的所有权限标识（通过角色链路） */
    @Select("SELECT DISTINCT ri.role_right FROM right_info ri " +
            "INNER JOIN role_right_info rri ON rri.right_id = ri.right_id " +
            "INNER JOIN user_role ur ON ur.role_id = rri.role_id " +
            "WHERE ur.uid = #{uid}")
    List<String> findPermCodesByUid(@Param("uid") String uid);

    /** 获取用户的所有角色 code（通过 user_role 链路） */
    @Select("SELECT DISTINCT ri.role FROM role_info ri " +
            "INNER JOIN user_role ur ON ur.role_id = ri.role_id " +
            "WHERE ur.uid = #{uid}")
    List<String> findRoleCodesByUid(@Param("uid") String uid);

    /** 获取角色的所有权限 */
    @Select("SELECT ri.* FROM right_info ri " +
            "INNER JOIN role_right_info rri ON rri.right_id = ri.right_id " +
            "WHERE rri.role_id = #{roleId}")
    List<RightInfo> findByRoleId(@Param("roleId") String roleId);
}
