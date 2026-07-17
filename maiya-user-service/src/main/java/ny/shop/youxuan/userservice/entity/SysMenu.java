package ny.shop.youxuan.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 系统菜单
 */
@Data
@TableName("sys_menu")
public class SysMenu {
    private Integer id;
    private Integer parentId;
    private String name;
    private String path;
    private String component;
    private String icon;
    private String permCode;
    private Integer sortOrder;
    private Integer menuType;   // 1=目录 2=菜单 3=按钮
    private Integer visible;
    private Integer status;
    private Long createTime;

    /** 子菜单（非数据库字段，树形结构用） */
    @TableField(exist = false)
    private List<SysMenu> children;
}
