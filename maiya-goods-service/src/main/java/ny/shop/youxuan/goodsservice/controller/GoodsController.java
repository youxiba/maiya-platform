package ny.shop.youxuan.goodsservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.goodsservice.entity.*;
import ny.shop.youxuan.goodsservice.service.GoodsService;
import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    @Autowired
    private GoodsService gs;

    @GetMapping("/{id}")
    public ApiResult<GoodsInfo> getGoods(@PathVariable String id) {
        return ApiResult.success(gs.getByGoodsInfoId(id));
    }

    @GetMapping("/mid/{mid}")
    public ApiResult<List<GoodsInfo>> getByMerchant(@PathVariable String mid) {
        return ApiResult.success(gs.getOnSaleByMid(mid));
    }

    @GetMapping("/categories")
    public ApiResult<List<GoodsCategory>> getCategories() {
        return ApiResult.success(gs.getAllCategories());
    }
}