package ny.shop.youxuan.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.userservice.entity.SysMenu;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /** 查询所有启用的菜单 */
    @Select("SELECT * FROM sys_menu WHERE status = 1 ORDER BY parent_id, sort_order")
    List<SysMenu> findAllEnabled();

    /** 查询顶级菜单 */
    @Select("SELECT * FROM sys_menu WHERE parent_id = 0 AND status = 1 ORDER BY sort_order")
    List<SysMenu> findTopMenus();

    /** 查询子菜单 */
    @Select("SELECT * FROM sys_menu WHERE parent_id = #{parentId} AND status = 1 ORDER BY sort_order")
    List<SysMenu> findByParentId(@Param("parentId") Integer parentId);
}
