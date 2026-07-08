package ny.shop.youxuan.notificationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.notificationservice.service.NotificationService;

@RestController
@RequestMapping("/internal/notify")
public class InternalNotificationController {
    @Autowired
    private NotificationService ns;

    @PostMapping("/print")
    public ApiResult<Boolean> print(@RequestParam String orderId, @RequestParam String mid) {
        return ApiResult.success(ns.printTicket(orderId, mid));
    }
}