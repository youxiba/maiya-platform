package ny.shop.youxuan.financeservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.financeservice.entity.WalletInfo;
import ny.shop.youxuan.financeservice.mapper.WalletInfoMapper;

@RestController
@RequestMapping("/internal/finance")
public class InternalFinanceController {
    @Autowired
    private WalletInfoMapper wMapper;

    @GetMapping("/wallet/{uid}")
    public ApiResult<WalletInfo> getWallet(@PathVariable String uid) {
        return ApiResult.success(wMapper.findByUid(uid));
    }
}