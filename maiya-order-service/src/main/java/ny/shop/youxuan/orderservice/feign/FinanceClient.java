package ny.shop.youxuan.orderservice.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;

@FeignClient(name = "maiya-finance-service", path = "/internal/finance")
public interface FinanceClient {

    @GetMapping("/wallet/{uid}")
    ApiResult<?> getWallet(@PathVariable("uid") String uid);
}