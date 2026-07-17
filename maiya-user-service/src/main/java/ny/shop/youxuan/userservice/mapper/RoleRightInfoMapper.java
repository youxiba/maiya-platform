package ny.shop.youxuan.userservice.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.userservice.entity.RoleRightInfo;

import java.util.List;

@Mapper
public interface RoleRightInfoMapper extends BaseMapper<RoleRightInfo> {

    /** 批量添加角色权限关联 */
    @Insert({
        "<script>",
        "INSERT INTO role_right_info (role_id, right_id) VALUES ",
        "<foreach collection='list' item='item' separator=','>",
        "(#{item.roleId}, #{item.rightId})",
        "</foreach>",
        "</script>"
    })
    int batchInsert(@Param("list") List<RoleRightInfo> list);

    /** 删除角色的所有权限关联 */
    @Delete("DELETE FROM role_right_info WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") String roleId);

    /** 查询角色的权限列表（含权限详情） */
    @Select("SELECT ri.*, rri.role_id FROM right_info ri " +
            "INNER JOIN role_right_info rri ON rri.right_id = ri.right_id " +
            "WHERE rri.role_id = #{roleId}")
    List<JSONObject> findByRoleId(@Param("roleId") String roleId);
}
