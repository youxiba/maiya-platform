package ny.shop.youxuan.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.paymentservice.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private PaymentService ps;

    @PostMapping("/unified")
    public ApiResult<com.alibaba.fastjson.JSONObject> pay(@RequestParam String orderId, @RequestParam String payType,
            @RequestParam Integer fee, HttpServletRequest req) {
        return ApiResult.success(ps.unifiedPay(orderId, payType, fee, "", req.getRemoteAddr()));
    }

    @PostMapping("/notify/alipay")
    public String aliNotify(HttpServletRequest req, HttpServletResponse resp) {
        return ps.handleAliNotify(req, resp);
    }

    @PostMapping("/notify/wxpay")
    public String wxNotify(HttpServletRequest req, HttpServletResponse resp) {
        return ps.handleWxNotify(req, resp);
    }
}