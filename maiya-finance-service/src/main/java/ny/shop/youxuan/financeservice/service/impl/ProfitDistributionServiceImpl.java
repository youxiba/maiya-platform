package ny.shop.youxuan.financeservice.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ny.shop.youxuan.financeservice.mapper.WalletInfoMapper;
import ny.shop.youxuan.financeservice.service.ProfitDistributionService;
@Service
public class ProfitDistributionServiceImpl implements ProfitDistributionService {
    @Autowired private WalletInfoMapper wMapper;
    @Override @Transactional
    public boolean settleOrder(String orderId, String uid, String mid, String mchUid, String superUid, com.alibaba.fastjson.JSONObject params) {
        return true;
    }
}