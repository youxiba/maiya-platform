package ny.shop.youxuan.financeservice.service;
public interface ProfitDistributionService {
    boolean settleOrder(String orderId, String uid, String mid, String mchUid, String superUid, com.alibaba.fastjson.JSONObject params);
}