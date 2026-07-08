package ny.shop.youxuan.marketingservice.service;

import com.alibaba.fastjson.JSONObject;

/** 转盘抽奖服务 */
public interface LotteryService {

    /** 获取抽奖页面数据（活动+奖品+我的次数+中奖名单） */
    JSONObject getLotteryData(String uid);

    /** 执行抽奖 */
    PrizeResult draw(String uid);
}
