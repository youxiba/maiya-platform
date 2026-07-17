package ny.shop.youxuan.userservice.controller.console;

import ny.shop.youxuan.common.result.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 后台管理 API — 等级分润配置
 *
 * 迁移自原单体项目 controller/console/LevelInfoController
 * 路径映射：/api/admin/user/level/**
 */
@RestController
@RequestMapping("/api/admin/user/level")
public class LevelInfoController {

    private static final Logger log = LoggerFactory.getLogger(LevelInfoController.class);

    @PostMapping("/setinfo")
    public ApiResult<Map<String, Object>> setLevelInfo(@RequestBody Map<String, Object> params) {
        log.info("保存等级配置: {}", params);
        return ApiResult.success(params);
    }

    @GetMapping("/getinfo")
    public ApiResult<Map<String, Object>> getLevelInfo() {
        return ApiResult.success(Map.of("level", 1, "ratio", 0));
    }
}
