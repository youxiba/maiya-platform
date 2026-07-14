package ny.shop.youxuan.marketingservice.controller;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.marketingservice.circuitbreaker.DegradeConfig;
import ny.shop.youxuan.marketingservice.circuitbreaker.FlashCircuitBreaker;
import ny.shop.youxuan.marketingservice.entity.FlashSale;
import ny.shop.youxuan.marketingservice.entity.RotationInfo;
import ny.shop.youxuan.marketingservice.service.FlashSaleHighConcurrencyService;
import ny.shop.youxuan.marketingservice.service.LotteryService;
import ny.shop.youxuan.marketingservice.service.MarketingService;
import ny.shop.youxuan.marketingservice.service.PrizeResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 营销服务控制器（带熔断降级）
 */
@RestController
@RequestMapping("/api/marketing")
public class MarketingController {

    private static final Logger log = LoggerFactory.getLogger(MarketingController.class);

    @Autowired
    private MarketingService marketingService;

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private FlashSaleHighConcurrencyService flashSaleService;

    @Autowired
    private FlashCircuitBreaker circuitBreaker;

    @Autowired
    private DegradeConfig degradeConfig;

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

    /**
     * 秒杀抢购（带熔断降级）
     */
    @PostMapping("/flash/grab")
    public ApiResult<JSONObject> grabFlashSale(
            @RequestParam String fsId,
            @RequestParam String uid,
            @RequestHeader(value = "X-Request-Id", required = false) String requestId,
            HttpServletRequest servletRequest) {

        if (requestId == null || requestId.isEmpty()) {
            requestId = java.util.UUID.randomUUID().toString().replace("-", "");
        }

        // ---- 1. 检查全局降级 ----
        if (degradeConfig.shouldDegrade(uid)) {
            log.warn("全局降级中, 返回排队: uid={}, fsId={}", uid, fsId);
            return ApiResult.success(buildQueuingResponse(fsId, uid, requestId));
        }

        // ---- 2. 检查熔断器 ----
        if (!circuitBreaker.tryAcquire()) {
            log.warn("熔断器打开, 返回排队: uid={}, fsId={}", uid, fsId);
            return ApiResult.success(buildQueuingResponse(fsId, uid, requestId));
        }

        // ---- 3. 正常执行 ----
        try {
            JSONObject result = flashSaleService.grabFlashSale(fsId, uid, requestId);
            circuitBreaker.onSuccess();
            return ApiResult.success(result);

        } catch (DataAccessException e) {
            // DB 异常 → 触发熔断
            circuitBreaker.onFailure();
            log.error("DB异常, 触发熔断: fsId={}, uid={}", fsId, uid, e);
            return ApiResult.success(buildQueuingResponse(fsId, uid, requestId));

        } catch (Exception e) {
            circuitBreaker.onFailure();
            log.error("抢购异常: fsId={}, uid={}", fsId, uid, e);
            return ApiResult.error("系统繁忙，请稍后重试");
        }
    }

    @GetMapping("/flash/result")
    public ApiResult<JSONObject> getFlashResult(@RequestParam String requestId) {
        JSONObject r = new JSONObject();
        r.put("requestId", requestId);
        r.put("status", "PROCESSING");
        r.put("msg", "排队中，请稍后查询订单");
        return ApiResult.success(r);
    }

    /**
     * 降级/运维端点：查看熔断器状态
     */
    @GetMapping("/flash/degrade/status")
    public ApiResult<Map<String, Object>> degradeStatus() {
        Map<String, Object> status = new java.util.LinkedHashMap<>();
        status.put("circuitBreaker", circuitBreaker.getState().name());
        status.put("failureRate", String.format("%.1f%%", circuitBreaker.getFailureRate() * 100));
        status.put("circuitBreakCount", circuitBreaker.getCircuitBreakCount());
        status.put("globalDegrade", degradeConfig.isGlobalDegrade());
        return ApiResult.success(status);
    }

    /**
     * 运维端点：手动切换降级
     */
    @PostMapping("/flash/degrade/toggle")
    public ApiResult<String> toggleDegrade(@RequestParam boolean enable) {
        degradeConfig.setGlobalDegrade(enable);
        log.warn("手动降级: {}", enable);
        return ApiResult.success(enable ? "降级已开启" : "降级已关闭");
    }

    /**
     * 运维端点：手动重置熔断器
     */
    @PostMapping("/flash/degrade/reset")
    public ApiResult<String> resetCircuitBreaker() {
        circuitBreaker.reset();
        return ApiResult.success("熔断器已重置");
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

    /**
     * 构建排队中响应
     */
    private JSONObject buildQueuingResponse(String fsId, String uid, String requestId) {
        JSONObject resp = new JSONObject();
        resp.put("code", 0);
        resp.put("msg", "抢购成功，正在排队处理");
        resp.put("queuing", true);
        resp.put("estimated", degradeConfig.getEstimatedWaitMs());
        resp.put("requestId", requestId);
        return resp;
    }
}
