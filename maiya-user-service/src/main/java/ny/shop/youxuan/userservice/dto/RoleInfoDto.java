package ny.shop.youxuan.userservice.dto;

import lombok.Data;

import java.util.List;

/**
 * 角色信息传输对象
 */
@Data
public class RoleInfoDto {
    private String uid;
    private String roleId;
    private Boolean roleStatus;
    private String roleName;
    private List<String> rightIds;
}
