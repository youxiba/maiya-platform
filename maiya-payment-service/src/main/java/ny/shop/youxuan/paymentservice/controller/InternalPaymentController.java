package ny.shop.youxuan.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/internal/payment")
public class InternalPaymentController {
    @Autowired
    private PaymentService ps;

    @PostMapping("/refund")
    public ApiResult<Boolean> refund(@RequestParam String orderId, @RequestParam String payType,
            @RequestParam String fee) {
        return ApiResult.success(true);
    }
}