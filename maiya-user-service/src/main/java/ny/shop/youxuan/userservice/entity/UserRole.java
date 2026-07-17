package ny.shop.youxuan.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户-角色关联
 */
@Data
@TableName("user_role")
public class UserRole {
    private Integer id;
    private String uid;
    private String roleId;
}
