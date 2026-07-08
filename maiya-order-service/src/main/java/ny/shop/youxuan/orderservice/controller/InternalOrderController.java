package ny.shop.youxuan.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.orderservice.service.OrderService;

@RestController
@RequestMapping("/internal/orders")
public class InternalOrderController {
    @Autowired
    private OrderService os;

    @GetMapping("/{id}")
    public ApiResult<ny.shop.youxuan.orderservice.entity.OrderInfo> getOrder(@PathVariable String id) {
        return ApiResult.success(os.getByOrderId(id));
    }

    @PostMapping("/pay")
    public ApiResult<Boolean> payNotify(@RequestParam String orderId, @RequestParam String transactionId,
            @RequestParam Integer payType) {
        return ApiResult.success(os.processPayment(orderId, transactionId, payType));
    }
}