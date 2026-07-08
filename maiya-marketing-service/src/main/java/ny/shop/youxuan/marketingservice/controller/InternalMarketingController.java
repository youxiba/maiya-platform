package ny.shop.youxuan.marketingservice.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.marketingservice.service.LotteryService;
import ny.shop.youxuan.marketingservice.service.MarketingService;
import ny.shop.youxuan.marketingservice.service.PrizeResult;

@RestController
@RequestMapping("/internal/marketing")
public class InternalMarketingController {

    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private MarketingService marketingService;

    @GetMapping("/lottery/data")
    public ApiResult<JSONObject> getLotteryData(@RequestParam String uid) {
        return ApiResult.success(lotteryService.getLotteryData(uid));
    }

    @PostMapping("/lottery/draw")
    public ApiResult<PrizeResult> draw(@RequestParam String uid) {
        return ApiResult.success(lotteryService.draw(uid));
    }

    @PostMapping("/coupon/claim")
    public ApiResult<Boolean> claimCoupon(@RequestParam String uid, @RequestParam String couponId) {
        return ApiResult.success(marketingService.claimCoupon(uid, couponId));
    }

    @PostMapping("/coupon/use")
    public ApiResult<Boolean> useCoupon(@RequestParam String uid, @RequestParam String couponId,
            @RequestParam String orderId) {
        return ApiResult.success(marketingService.useCoupon(uid, couponId, orderId));
    }
}
