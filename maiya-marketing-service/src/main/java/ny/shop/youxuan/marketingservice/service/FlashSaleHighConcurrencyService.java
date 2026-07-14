package ny.shop.youxuan.marketingservice.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 秒杀高并发服务
 */
public interface FlashSaleHighConcurrencyService {

    /** 秒杀前：商品库存预热到 Redis */
    boolean preheatStock(String fsId);

    /** 秒杀中：获取秒杀令牌（含 Redis Lua 扣库存） */
    JSONObject grabFlashSale(String fsId, String uid, String requestId);

    /** 库存回滚（超时未支付 / 取消订单） */
    boolean rollbackStock(String fsId, String uid, int qty);
}
