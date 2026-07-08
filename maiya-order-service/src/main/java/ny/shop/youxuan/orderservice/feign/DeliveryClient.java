package ny.shop.youxuan.orderservice.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;

@FeignClient(name = "maiya-delivery-service", path = "/internal/delivery")
public interface DeliveryClient {

    @PostMapping("/create")
    ApiResult<Boolean> createDelivery(@RequestBody com.alibaba.fastjson.JSONObject info);

    @PostMapping("/status")
    ApiResult<Boolean> updateStatus(@RequestParam String id, @RequestParam int status);
}