package ny.shop.youxuan.deliveryservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.deliveryservice.entity.DtbtInfo;
import ny.shop.youxuan.deliveryservice.service.DeliveryService;

@RestController
@RequestMapping("/internal/delivery")
public class InternalDeliveryController {
    @Autowired
    private DeliveryService ds;

    @PostMapping("/create")
    public ApiResult<Boolean> create(@RequestBody DtbtInfo info) {
        return ApiResult.success(ds.createDelivery(info));
    }

    @PostMapping("/status")
    public ApiResult<Boolean> updateStatus(@RequestParam String id, @RequestParam int status) {
        return ApiResult.success(ds.updateStatus(id, status));
    }
}