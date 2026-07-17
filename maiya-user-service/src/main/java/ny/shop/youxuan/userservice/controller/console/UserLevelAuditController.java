package ny.shop.youxuan.userservice.controller.console;

import com.alibaba.fastjson.JSONObject;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.common.result.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理 API — 会员审核
 *
 * 迁移自原单体项目 controller/console/UserLevelAuditController
 * 路径映射：/api/admin/user/levelaudit/**
 */
@RestController
@RequestMapping("/api/admin/user/levelaudit")
public class UserLevelAuditController {

    private static final Logger log = LoggerFactory.getLogger(UserLevelAuditController.class);

    @GetMapping("/setlevel")
    public ApiResult<JSONObject> setLevel(@RequestParam String uid) {
        log.info("用户等级审核申请: uid={}", uid);
        return ApiResult.success(new JSONObject());
    }

    @GetMapping("/auditinfo")
    public ApiResult<JSONObject> auditInfo(
            @RequestParam String infoId,
            @RequestParam String auditStatus) {
        log.info("审核操作: infoId={}, status={}", infoId, auditStatus);
        return ApiResult.success(new JSONObject());
    }

    @GetMapping("/getall")
    public ApiResult<PageResult<JSONObject>> getAll(
            @RequestParam int pageIndex,
            @RequestParam int pageSize) {
        return ApiResult.success(new PageResult<>(List.of(), 0, pageIndex, pageSize));
    }

    @GetMapping("/getbyuid")
    public ApiResult<List<JSONObject>> getByUid(@RequestParam String uid) {
        log.info("查询用户审核记录: uid={}", uid);
        return ApiResult.success(List.of());
    }

    @GetMapping("/getbyuidandlev")
    public ApiResult<JSONObject> getByUidAndLevel(
            @RequestParam String uid,
            @RequestParam int level) {
        log.info("查询用户等级审核: uid={}, level={}", uid, level);
        return ApiResult.success(new JSONObject());
    }
}
