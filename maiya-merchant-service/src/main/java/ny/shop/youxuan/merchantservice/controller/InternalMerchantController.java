package ny.shop.youxuan.merchantservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.dto.MerchantDto;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.merchantservice.entity.MerchantInfo;
import ny.shop.youxuan.merchantservice.service.MerchantInfoService;
import ny.shop.youxuan.merchantservice.service.MerchantHoursService;

@RestController
@RequestMapping("/internal/merchants")
public class InternalMerchantController {
    @Autowired
    private MerchantInfoService merchantService;
    @Autowired
    private MerchantHoursService hoursService;

    @GetMapping("/{mid}")
    public ApiResult<MerchantDto> getMerchant(@PathVariable String mid) {
        MerchantInfo m = merchantService.getByMid(mid);
        return m == null ? ApiResult.error(5000, "不存在") : ApiResult.success(toDto(m));
    }

    @GetMapping("/by-uid")
    public ApiResult<MerchantDto> getByUid(@RequestParam String uid) {
        MerchantInfo m = merchantService.getByUid(uid);
        return m == null ? ApiResult.error(5000, "不存在") : ApiResult.success(toDto(m));
    }

    @GetMapping("/{mid}/store-name")
    public ApiResult<String> getStoreName(@PathVariable String mid) {
        MerchantInfo m = merchantService.getByMid(mid);
        return ApiResult.success(m != null ? m.getStoreName() : "");
    }

    @GetMapping("/{mid}/open-status")
    public ApiResult<Boolean> getOpenStatus(@PathVariable String mid) {
        return ApiResult.success(hoursService.isOpen(mid));
    }

    @GetMapping("/{mid}/uid")
    public ApiResult<String> getUidByMid(@PathVariable String mid) {
        return ApiResult.success(merchantService.getUidByMid(mid));
    }

    private MerchantDto toDto(MerchantInfo m) {
        MerchantDto d = new MerchantDto();
        d.setMid(m.getMid());
        d.setUid(m.getUid());
        d.setStoreName(m.getStoreName());
        d.setTelephone(m.getTelephone());
        d.setLogo(m.getLogo());
        d.setLon(m.getLon());
        d.setLat(m.getLat());
        d.setOnline(m.getOnline());
        return d;
    }
}