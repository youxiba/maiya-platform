package ny.shop.youxuan.marketingservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ny.shop.youxuan.marketingservice.entity.*;
import ny.shop.youxuan.marketingservice.mapper.*;
import ny.shop.youxuan.marketingservice.service.LotteryService;
import ny.shop.youxuan.marketingservice.service.PrizeResult;
import java.util.*;

/** 转盘抽奖实现 - 含累积概率算法 */
@Service
public class LotteryServiceImpl implements LotteryService {

    private static final Logger log = LoggerFactory.getLogger(LotteryServiceImpl.class);

    @Autowired private DrawInfoMapper drawInfoMapper;
    @Autowired private FlashSaleMapper flashSaleMapper;

    // 简化实现：在实际项目中需注入完整 Mapper 集合

    @Override
    public JSONObject getLotteryData(String uid) {
        JSONObject result = new JSONObject();
        result.put("opportunitySum", 1); // 示例：1次抽奖机会
        result.put("falsedataList", new ArrayList<>());
        result.put("prizeList", new ArrayList<>());
        return result;
    }

    /**
     * 核心抽奖算法 - 累积概率区间法
     *
     * 原理：将每个奖品的概率映射为 [0,1) 上的累积区间，
     * 生成随机数后通过插入排序找到落点下标
     */
    @Override
    @Transactional
    public PrizeResult draw(String uid) {
        // 1. 查找进行中的活动
        // 2. 校验抽奖机会
        // 3. 获取奖品列表和概率
        // 4. 执行 lottery() 算法
        // 5. 更新库存、记录抽奖结果
        // 6. 返回奖品

        return new PrizeResult(); // 简化返回
    }

    /** 累积概率抽奖算法 */
    public static int lottery(List<Double> probabilities) {
        if (probabilities == null || probabilities.isEmpty()) return -1;

        double sumRate = 0;
        for (double p : probabilities) sumRate += p;
        if (sumRate <= 0) return -1;

        // 构建累积概率区间
        List<Double> cumulative = new ArrayList<>(probabilities.size());
        double tempSum = 0;
        for (double p : probabilities) {
            tempSum += p;
            cumulative.add(tempSum / sumRate);
        }

        // 随机数落在哪个区间
        double rand = Math.random();
        cumulative.add(rand);
        Collections.sort(cumulative);
        int index = cumulative.indexOf(rand);
        return index < probabilities.size() ? index : -1;
    }
}
