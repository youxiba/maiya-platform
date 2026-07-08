package ny.shop.youxuan.financeservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.dto.UserDto;
import ny.shop.youxuan.common.result.ApiResult;

@FeignClient(name = "maiya-user-service", path = "/internal/users")
public interface UserClient {
    @GetMapping("/{uid}")
    ApiResult<UserDto> getUser(@PathVariable("uid") String uid);

    @GetMapping("/by-phone")
    ApiResult<UserDto> getUserByPhone(@RequestParam("telephone") String telephone);

    @GetMapping("/superior/{uid}")
    ApiResult<String> getSuperUid(@PathVariable("uid") String uid);
}