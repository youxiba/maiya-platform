package ny.shop.youxuan.userservice.controller.app;

import ny.shop.youxuan.common.result.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户端 API — 关系信息
 *
 * 迁移自原单体项目 controller/UserGroupController
 * 路径映射：/api/user/group/**
 */
@RestController
@RequestMapping("/api/user/group")
public class UserGroupController {

    @GetMapping("/info")
    public ApiResult<List<String>> getGroupInfo() {
        return ApiResult.success(List.of());
    }
}
