package ny.shop.youxuan.notificationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.notificationservice.entity.MessageInfo;
import ny.shop.youxuan.notificationservice.mapper.MessageInfoMapper;
import java.util.List;

@RestController
@RequestMapping("/api/notify")
public class NotificationController {
    @Autowired
    private MessageInfoMapper msgMapper;

    @GetMapping("/messages/{uid}")
    public ApiResult<List<MessageInfo>> getMessages(@PathVariable String uid) {
        return ApiResult.success(msgMapper.findByUid(uid));
    }
}