package ny.shop.youxuan.userservice.controller.console;

import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.userservice.entity.RightInfo;
import ny.shop.youxuan.userservice.service.impl.RightInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理 API — 权限管理
 *
 * 路径映射：/api/admin/user/right/**
 */
@RestController
@RequestMapping("/api/admin/user/right")
public class RightInfoController {

    @Autowired
    private RightInfoServiceImpl rightInfoService;

    /** 添加权限 */
    @PostMapping("/add")
    public ApiResult<RightInfo> addRight(@RequestBody RightInfo rightInfo) {
        return rightInfoService.addRight(rightInfo);
    }

    /** 修改权限 */
    @PostMapping("/update")
    public ApiResult<RightInfo> updateRight(@RequestBody RightInfo rightInfo) {
        return rightInfoService.updateRight(rightInfo);
    }

    /** 删除权限 */
    @GetMapping("/del")
    public ApiResult<Boolean> deleteRight(@RequestParam String rightId) {
        return rightInfoService.deleteRight(rightId);
    }

    /** 查询权限 */
    @GetMapping("/getbyid")
    public ApiResult<RightInfo> getByRightId(@RequestParam String rightId) {
        return rightInfoService.getByRightId(rightId);
    }

    /** 查询所有权限 */
    @GetMapping("/getall")
    public ApiResult<List<RightInfo>> getAll() {
        return rightInfoService.getAll();
    }
}
