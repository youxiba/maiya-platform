package ny.shop.youxuan.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.userservice.entity.UserRole;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /** 查询用户的角色关联 */
    @Select("SELECT * FROM user_role WHERE uid = #{uid}")
    List<UserRole> findByUid(@Param("uid") String uid);

    /** 删除用户的所有角色 */
    @Delete("DELETE FROM user_role WHERE uid = #{uid}")
    int deleteByUid(@Param("uid") String uid);

    /** 批量添加用户角色 */
    @Insert({
        "<script>",
        "INSERT INTO user_role (uid, role_id) VALUES ",
        "<foreach collection='list' item='item' separator=','>",
        "(#{item.uid}, #{item.roleId})",
        "</foreach>",
        "</script>"
    })
    int batchInsert(@Param("list") List<UserRole> list);
}
