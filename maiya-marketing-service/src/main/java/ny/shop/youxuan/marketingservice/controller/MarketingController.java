package ny.shop.youxuan.marketingservice.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.marketingservice.entity.FlashSale;
import ny.shop.youxuan.marketingservice.entity.RotationInfo;
import ny.shop.youxuan.marketingservice.service.FlashSaleHighConcurrencyService;
import ny.shop.youxuan.marketingservice.service.LotteryService;
import ny.shop.youxuan.marketingservice.service.MarketingService;
import ny.shop.youxuan.marketingservice.service.PrizeResult;

import java.util.List;

@RestController
@RequestMapping("/api/marketing")
public class MarketingController {

    @Autowired
    private MarketingService marketingService;
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private FlashSaleHighConcurrencyService flashSaleService;

    @GetMapping("/flashsales")
    public ApiResult<List<FlashSale>> getFlashSales() {
        return ApiResult.success(marketingService.getActiveFlashSales());
    }

    @GetMapping("/banners")
    public ApiResult<List<RotationInfo>> getBanners() {
        return ApiResult.success(marketingService.getActiveBanners());
    }

    @PostMapping("/flash/preheat")
    public ApiResult<Boolean> preheatStock(@RequestParam String fsId) {
        return ApiResult.success(flashSaleService.preheatStock(fsId));
    }

    @PostMapping("/flash/grab")
    public ApiResult<JSONObject> grabFlashSale(@RequestParam String fsId, @RequestParam String uid) {
        return ApiResult.success(flashSaleService.grabFlashSale(fsId, uid));
    }

    @GetMapping("/flash/result")
    public ApiResult<String> getFlashResult(@RequestParam String requestId) {
        return ApiResult.success("排队中，请稍后查询订单");
    }

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
}
