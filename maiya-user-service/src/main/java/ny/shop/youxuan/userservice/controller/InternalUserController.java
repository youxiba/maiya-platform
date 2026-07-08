package ny.shop.youxuan.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.dto.UserDto;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.userservice.entity.UserInfo;
import ny.shop.youxuan.userservice.service.AuthService;
import ny.shop.youxuan.userservice.service.UserInfoService;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {
    @Autowired
    private UserInfoService userService;
    @Autowired
    private AuthService authService;

    @GetMapping("/{uid}")
    public ApiResult<UserDto> getUser(@PathVariable String uid) {
        UserInfo u = userService.getByUid(uid);
        if (u == null)
            return ApiResult.error(2000, "不存在");
        return ApiResult.success(toDto(u));
    }

    @GetMapping("/by-phone")
    public ApiResult<UserDto> getByPhone(@RequestParam String telephone) {
        UserInfo u = userService.getByTelephone(telephone);
        if (u == null)
            return ApiResult.error(2000, "不存在");
        return ApiResult.success(toDto(u));
    }

    @GetMapping("/superior/{uid}")
    public ApiResult<String> getSuperUid(@PathVariable String uid) {
        return ApiResult.success(userService.getSuperUid(uid));
    }

    @GetMapping("/verify-token")
    public ApiResult<String> verifyToken(@RequestParam String token) {
        String uid = authService.verifyToken(token);
        if (uid == null)
            return ApiResult.error(2006, "Token无效");
        return ApiResult.success(uid);
    }

    private UserDto toDto(UserInfo u) {
        UserDto d = new UserDto();
        d.setUid(u.getUid());
        d.setUsername(u.getUsername());
        d.setNickname(u.getNickname());
        d.setAvatar(u.getAvatar());
        d.setTelephone(u.getTelephone());
        d.setSuperUid(u.getSuperUid());
        d.setInviteCode(u.getInviteCode());
        d.setLevel(u.getLevel());
        return d;
    }
}