package ny.shop.youxuan.orderservice.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;

@FeignClient(name = "maiya-notification-service", path = "/internal/notify")
public interface NotificationClient {

    @PostMapping("/print")
    ApiResult<Boolean> printTicket(@RequestParam String orderId, @RequestParam String mid);

    @PostMapping("/message")
    ApiResult<Boolean> sendMessage(@RequestBody com.alibaba.fastjson.JSONObject message);

    @PostMapping("/push")
    ApiResult<Boolean> push(@RequestParam String uid, @RequestParam String title, @RequestParam String text);
}