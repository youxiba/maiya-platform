package ny.shop.youxuan.marketingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.marketingservice.service.admin.VipListService;

import java.util.Map;

/**
 * 秒杀系统管理控制器
 *
 * 提供 VIP 名单管理、限流状态查询等运维功能。
 * 仅限于内网或管理网络访问。
 */
@RestController
@RequestMapping("/admin/flash")
public class AdminController {

    @Autowired
    private VipListService vipListService;

    // ======== VIP 名单管理 ========

    @PostMapping("/vip/add")
    public ApiResult<Boolean> addVip(@RequestParam String uid) {
        return ApiResult.success(vipListService.addVip(uid));
    }

    @PostMapping("/vip/add/batch")
    public ApiResult<Integer> batchAddVip(@RequestParam String uids) {
        return ApiResult.success(vipListService.batchAddVipFromString(uids));
    }

    @PostMapping("/vip/remove")
    public ApiResult<Boolean> removeVip(@RequestParam String uid) {
        return ApiResult.success(vipListService.removeVip(uid));
    }

    @GetMapping("/vip/check")
    public ApiResult<Boolean> checkVip(@RequestParam String uid) {
        return ApiResult.success(vipListService.isVip(uid));
    }

    @GetMapping("/vip/list")
    public ApiResult<Map<Object, Object>> listVips() {
        return ApiResult.success(vipListService.getAllVips());
    }

    @GetMapping("/vip/count")
    public ApiResult<Long> vipCount() {
        return ApiResult.success(vipListService.getVipCount());
    }
}
