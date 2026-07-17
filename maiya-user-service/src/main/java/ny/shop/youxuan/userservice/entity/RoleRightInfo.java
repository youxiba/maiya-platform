package ny.shop.youxuan.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色-权限关联
 */
@Data
@TableName("role_right_info")
public class RoleRightInfo {
    private Integer id;
    private String roleId;
    private String rightId;
}
