package ny.shop.youxuan.userservice.controller.app;

import com.alibaba.fastjson.JSONObject;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.userservice.entity.UserInfo;
import ny.shop.youxuan.userservice.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户端 API（app/小程序）
 *
 * 迁移自原单体项目 controller/UserInfoController
 * 路径映射：/api/user/**
 */
@RestController
@RequestMapping("/api/user")
public class AppUserInfoController {

    private static final Logger log = LoggerFactory.getLogger(AppUserInfoController.class);

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/getbyuid")
    public ApiResult<UserInfo> getByUid(@RequestParam String uid) {
        UserInfo info = userInfoService.getByUid(uid);
        if (info == null) return ApiResult.error("用户不存在");
        return ApiResult.success(info);
    }

    @GetMapping("/checkusername")
    public ApiResult<Boolean> checkUsername(@RequestParam String username) {
        return ApiResult.success(true);
    }

    @GetMapping("/resettoken")
    public ApiResult<JSONObject> resetToken(HttpServletRequest request) {
        log.info("刷新Token");
        return ApiResult.success(new JSONObject());
    }
}
