package ny.shop.youxuan.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色信息
 */
@Data
@TableName("role_info")
public class RoleInfo {
    private Integer id;
    private Long createTime;
    private String uid;
    private String roleId;
    private String role;
    private String roleName;
    private Boolean roleStatus;
}
