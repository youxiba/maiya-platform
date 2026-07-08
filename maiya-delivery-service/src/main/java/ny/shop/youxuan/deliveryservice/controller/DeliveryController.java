package ny.shop.youxuan.deliveryservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.deliveryservice.entity.DtbtInfo;
import ny.shop.youxuan.deliveryservice.entity.RiderInfo;
import ny.shop.youxuan.deliveryservice.mapper.DtbtInfoMapper;
import ny.shop.youxuan.deliveryservice.mapper.RiderInfoMapper;
import java.util.List;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {
    @Autowired
    private DtbtInfoMapper dtbtMapper;
    @Autowired
    private RiderInfoMapper riderMapper;

    @GetMapping("/{orderId}")
    public ApiResult<List<DtbtInfo>> getDelivery(@PathVariable String orderId) {
        return ApiResult.success(dtbtMapper.findByOrderId(orderId));
    }

    @GetMapping("/rider/{rid}")
    public ApiResult<RiderInfo> getRider(@PathVariable String rid) {
        return ApiResult.success(riderMapper.findByRid(rid));
    }
}