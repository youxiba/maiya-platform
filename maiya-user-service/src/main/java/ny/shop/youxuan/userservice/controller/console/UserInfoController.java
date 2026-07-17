package ny.shop.youxuan.userservice.controller.console;

import com.alibaba.fastjson.JSONObject;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.common.result.PageResult;
import ny.shop.youxuan.userservice.entity.UserInfo;
import ny.shop.youxuan.userservice.service.UserInfoService;
import ny.shop.youxuan.userservice.vo.AdminUserInfoVO;
import ny.shop.youxuan.userservice.vo.UserInfoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 后台管理 API — 用户信息
 *
 * 迁移自原单体项目 controller/console/UserInfoController
 * 路径映射：/api/admin/user/**
 */
@RestController
@RequestMapping("/api/admin/user")
public class UserInfoController {

    private static final Logger log = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/getbyuid")
    public ApiResult<AdminUserInfoVO> getByUid(@RequestParam String uid) {
        UserInfo info = userInfoService.getByUid(uid);
        if (info == null) return ApiResult.error("用户不存在");
        return ApiResult.success(UserInfoConverter.toAdminVO(info));
    }

    @GetMapping("/getsubusers")
    public ApiResult<List<JSONObject>> getSubUsers(@RequestParam String uid) {
        log.info("查询推荐用户: uid={}", uid);
        return ApiResult.success(List.of());
    }

    @GetMapping("/getztusers")
    public ApiResult<JSONObject> getZtUsers(@RequestParam String uid) {
        log.info("查询直推用户: uid={}", uid);
        return ApiResult.success(new JSONObject());
    }

    @GetMapping("/getjtusers")
    public ApiResult<JSONObject> getJtUsers(@RequestParam String uid) {
        log.info("查询间推用户: uid={}", uid);
        return ApiResult.success(new JSONObject());
    }

    @GetMapping("/getrightsbyuid")
    public ApiResult<JSONObject> getRightsByUid(@RequestParam String uid) {
        JSONObject r = new JSONObject();
        r.put("uid", uid);
        return ApiResult.success(r);
    }

    @GetMapping("/getallinfopage")
    public ApiResult<PageResult<AdminUserInfoVO>> getAllUserInfoPage(
            @RequestParam int pageIndex, @RequestParam int pageSize) {
        return ApiResult.success(new PageResult<>(List.of(), 0, pageIndex, pageSize));
    }

    @GetMapping("/getalluser")
    public ApiResult<PageResult<AdminUserInfoVO>> getAllUser(
            @RequestParam int pageIndex, @RequestParam int pageSize) {
        return ApiResult.success(new PageResult<>(List.of(), 0, pageIndex, pageSize));
    }

    @PostMapping("/addadmin")
    public ApiResult<JSONObject> addAdmin(@RequestBody UserInfo userInfo) {
        log.info("添加管理员: username={}", userInfo.getUsername());
        return ApiResult.success(new JSONObject());
    }

    @GetMapping("/deladmin")
    public ApiResult<Boolean> delAdmin(@RequestParam String uid) {
        log.info("删除管理员: uid={}", uid);
        return ApiResult.success(true);
    }

    @GetMapping("/getbyusername")
    public ApiResult<AdminUserInfoVO> getByUsername(@RequestParam String username) {
        log.info("查询用户: username={}", username);
        return ApiResult.error("未实现");
    }

    @PostMapping("/setpassword")
    public ApiResult<Boolean> setPassword(@RequestBody Map<String, String> params) {
        log.info("修改密码: uid={}", params.get("uid"));
        return ApiResult.success(true);
    }

    @GetMapping("/setenable")
    public ApiResult<Boolean> setEnable(@RequestParam String uid, @RequestParam boolean enable) {
        log.info("设置用户状态: uid={}, enable={}", uid, enable);
        return ApiResult.success(true);
    }

    @PostMapping("/updateavatar")
    public ApiResult<JSONObject> updateAvatar(
            @RequestParam("file") MultipartFile file, @RequestParam String uid) {
        log.info("更新头像: uid={}", uid);
        return ApiResult.success(new JSONObject());
    }

    @GetMapping("/getusercount")
    public ApiResult<JSONObject> getUserCount() {
        return ApiResult.success(new JSONObject());
    }

    @GetMapping("/getnewuser")
    public ApiResult<List<Integer>> getNewUser(
            @RequestParam long startTime, @RequestParam int days) {
        return ApiResult.success(List.of());
    }
}
