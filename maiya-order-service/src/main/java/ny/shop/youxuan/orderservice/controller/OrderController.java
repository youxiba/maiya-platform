package ny.shop.youxuan.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.orderservice.entity.OrderInfo;
import ny.shop.youxuan.orderservice.service.OrderService;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService os;

    @PostMapping("/create")
    public ApiResult<String> create(@RequestBody OrderInfo order) {
        return ApiResult.success(os.createOrder(order));
    }

    @GetMapping("/{id}")
    public ApiResult<OrderInfo> get(@PathVariable String id) {
        return ApiResult.success(os.getByOrderId(id));
    }

    @GetMapping("/user/{uid}")
    public ApiResult<List<OrderInfo>> getUserOrders(@PathVariable String uid) {
        return ApiResult.success(os.getUserOrders(uid));
    }
}