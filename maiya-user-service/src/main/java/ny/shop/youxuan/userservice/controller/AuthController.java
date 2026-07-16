package ny.shop.youxuan.userservice.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.userservice.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/send-code")
    public ApiResult<Boolean> sendCode(@RequestParam String telephone) {
        return ApiResult.success(authService.sendPhoneCode(telephone));
    }

    @PostMapping("/login")
    public ApiResult<JSONObject> login(@RequestParam String telephone, @RequestParam String code) {
        return ApiResult.success(authService.loginByPhone(telephone, code));
    }

    @PostMapping("/register")
    public ApiResult<JSONObject> register(@RequestParam String telephone,
            @RequestParam(required = false) String inviteCode) {
        return ApiResult.success(authService.register(telephone, inviteCode));
    }

    @PostMapping("/login-by-pwd")
    public ApiResult<JSONObject> loginByPwd(@RequestParam String username, @RequestParam String password) {
        return ApiResult.success(authService.loginByPassword(username, password));
    }

    @PostMapping("/third-login")
    public ApiResult<JSONObject> thirdLogin(@RequestParam String thirdType, @RequestParam String unionId,
            @RequestParam(required = false) String inviteCode) {
        return ApiResult.success(authService.thirdPartyLogin(thirdType, unionId, inviteCode));
    }
}