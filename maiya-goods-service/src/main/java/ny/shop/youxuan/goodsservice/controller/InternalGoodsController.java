package ny.shop.youxuan.goodsservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.dto.GoodsDto;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.goodsservice.entity.GoodsInfo;
import ny.shop.youxuan.goodsservice.service.GoodsService;

@RestController
@RequestMapping("/internal/goods")
public class InternalGoodsController {
    @Autowired
    private GoodsService gs;

    @GetMapping("/{id}")
    public ApiResult<GoodsDto> getGoods(@PathVariable String id) {
        GoodsInfo g = gs.getByGoodsInfoId(id);
        if (g == null)
            return ApiResult.error(5000, "不存在");
        return ApiResult.success(toDto(g));
    }

    @PostMapping("/stock/deduct")
    public ApiResult<Boolean> deductStock(@RequestParam String id, @RequestParam Integer qty) {
        return ApiResult.success(gs.deductStock(id, qty));
    }

    @PostMapping("/stock/rollback")
    public ApiResult<Boolean> rollbackStock(@RequestParam String id, @RequestParam Integer qty) {
        return ApiResult.success(gs.rollbackStock(id, qty));
    }

    @PostMapping("/sales/add")
    public ApiResult<Boolean> addSales(@RequestParam String id, @RequestParam Integer qty) {
        return ApiResult.success(gs.addSales(id, qty));
    }

    private GoodsDto toDto(GoodsInfo g) {
        GoodsDto d = new GoodsDto();
        d.setGoodsInfoId(g.getGoodsInfoId());
        d.setGoodsName(g.getGoodsName());
        d.setPicsUrl(g.getPicsUrl());
        d.setGoodsPrice(g.getGoodsPrice());
        d.setActivePrice(g.getActivePrice());
        d.setSum(g.getSum());
        d.setSales(g.getSales());
        return d;
    }
}