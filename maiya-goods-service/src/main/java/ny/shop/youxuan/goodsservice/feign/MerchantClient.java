package ny.shop.youxuan.goodsservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.dto.MerchantDto;
import ny.shop.youxuan.common.result.ApiResult;

@FeignClient(name = "maiya-merchant-service", path = "/internal/merchants")
public interface MerchantClient {
    @GetMapping("/{mid}")
    ApiResult<MerchantDto> getMerchant(@PathVariable("mid") String mid);

    @GetMapping("/{mid}/store-name")
    ApiResult<String> getStoreName(@PathVariable("mid") String mid);
}