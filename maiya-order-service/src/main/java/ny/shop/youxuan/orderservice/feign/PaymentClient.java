package ny.shop.youxuan.orderservice.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;

@FeignClient(name = "maiya-payment-service", path = "/internal/payment")
public interface PaymentClient {

    @PostMapping("/refund")
    ApiResult<Boolean> refund(@RequestParam String orderId,
                              @RequestParam String subId,
                              @RequestParam String payType,
                              @RequestParam String fee);
}