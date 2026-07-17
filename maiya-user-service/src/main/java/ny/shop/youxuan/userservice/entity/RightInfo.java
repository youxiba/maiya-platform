package ny.shop.youxuan.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 权限标识
 */
@Data
@TableName("right_info")
public class RightInfo {
    private Integer id;
    private String rightId;
    private String roleRight;
    private String name;
}
