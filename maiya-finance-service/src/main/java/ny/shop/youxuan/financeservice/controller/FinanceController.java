package ny.shop.youxuan.financeservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.financeservice.entity.WalletInfo;
import ny.shop.youxuan.financeservice.mapper.WalletInfoMapper;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {
    @Autowired
    private WalletInfoMapper walletMapper;

    @GetMapping("/wallet/{uid}")
    public ApiResult<WalletInfo> getWallet(@PathVariable String uid) {
        return ApiResult.success(walletMapper.findByUid(uid));
    }

    @PostMapping("/wallet/recharge")
    public ApiResult<Boolean> recharge(@RequestParam String uid, @RequestParam BigDecimal amount) {
        return ApiResult.success(walletMapper.addMoney(uid, amount) > 0);
    }
}