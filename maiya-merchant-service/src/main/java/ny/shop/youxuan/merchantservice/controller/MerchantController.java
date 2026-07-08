package ny.shop.youxuan.merchantservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.merchantservice.entity.MerchantInfo;
import ny.shop.youxuan.merchantservice.service.MerchantInfoService;
import ny.shop.youxuan.merchantservice.service.MerchantHoursService;
import java.util.List;

@RestController
@RequestMapping("/api/merchant")
public class MerchantController {
    @Autowired
    private MerchantInfoService merchantService;
    @Autowired
    private MerchantHoursService hoursService;

    @GetMapping("/{mid}")
    public ApiResult<MerchantInfo> getMerchant(@PathVariable String mid) {
        MerchantInfo m = merchantService.getByMid(mid);
        return m == null ? ApiResult.error(5000, "不存在") : ApiResult.success(m);
    }

    @GetMapping("/nearby")
    public ApiResult<List<MerchantInfo>> getNearby(@RequestParam Double lon, @RequestParam Double lat,
            @RequestParam(defaultValue = "10000") Integer range) {
        return ApiResult.success(merchantService.findNearby(lon, lat, range));
    }

    @GetMapping("/{mid}/open")
    public ApiResult<Boolean> isOpen(@PathVariable String mid) {
        return ApiResult.success(hoursService.isOpen(mid));
    }

    @PostMapping
    public ApiResult<Boolean> create(@RequestBody MerchantInfo m) {
        return ApiResult.success(merchantService.addMerchant(m));
    }

    @PutMapping
    public ApiResult<Boolean> update(@RequestBody MerchantInfo m) {
        return ApiResult.success(merchantService.updateMerchant(m));
    }
}