package ny.shop.youxuan.orderservice.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.dto.GoodsDto;
import ny.shop.youxuan.common.result.ApiResult;

@FeignClient(name = "maiya-goods-service", path = "/internal/goods")
public interface GoodsClient {

    @GetMapping("/{goodsInfoId}")
    ApiResult<GoodsDto> getGoods(@PathVariable("goodsInfoId") String goodsInfoId);

    @PostMapping("/stock/deduct")
    ApiResult<Boolean> deductStock(@RequestParam String id, @RequestParam Integer qty);

    @PostMapping("/stock/rollback")
    ApiResult<Boolean> rollbackStock(@RequestParam String id, @RequestParam Integer qty);
}