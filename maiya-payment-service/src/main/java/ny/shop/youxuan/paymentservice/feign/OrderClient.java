package ny.shop.youxuan.paymentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;

@FeignClient(name = "maiya-order-service", path = "/internal/orders")
public interface OrderClient {

    @GetMapping("/{orderId}")
    ApiResult<?> getOrder(@PathVariable("orderId") String orderId);

    @PostMapping("/pay")
    ApiResult<Boolean> payNotify(@RequestParam String orderId,
            @RequestParam String transactionId,
            @RequestParam Integer payType);
}