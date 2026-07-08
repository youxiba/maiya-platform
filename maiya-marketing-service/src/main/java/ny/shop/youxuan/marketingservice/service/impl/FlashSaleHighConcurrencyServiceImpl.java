package ny.shop.youxuan.marketingservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.PostConstruct;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ny.shop.youxuan.marketingservice.entity.FlashSale;
import ny.shop.youxuan.marketingservice.mapper.FlashSaleMapper;
import ny.shop.youxuan.marketingservice.service.FlashSaleHighConcurrencyService;

import java.util.Arrays;
import java.util.List;

/**
 * 秒杀高并发实现
 *
 * 核心流程：预热→Redis Lua扣库存→MQ异步下单→超时回滚
 */
@Service
public class FlashSaleHighConcurrencyServiceImpl implements FlashSaleHighConcurrencyService {

    private static final Logger log = LoggerFactory.getLogger(FlashSaleHighConcurrencyServiceImpl.class);

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private FlashSaleMapper flashSaleMapper;
    @Autowired(required = false)
    private RocketMQTemplate rocketMQTemplate;

    private DefaultRedisScript<Long> stockLuaScript;

    private static final String KEY_STOCK = "flash:stock:";
    private static final String KEY_USERS = "flash:users:";
    private static final String KEY_ACTIVE = "flash:active:";
    private static final String KEY_BUY = "flash:buy:";

    @PostConstruct
    public void init() {
        stockLuaScript = new DefaultRedisScript<>();
        stockLuaScript.setLocation(new ClassPathResource("flash_stock.lua"));
        stockLuaScript.setResultType(Long.class);
    }

    @Override
    public boolean preheatStock(String fsId) {
        FlashSale sale = flashSaleMapper.findByFsId(fsId);
        if (sale == null || !sale.getEnable()) {
            log.warn("秒杀活动不可用: fsId={}", fsId);
            return false;
        }
        int totalStock = 100;
        redisTemplate.opsForValue().set(KEY_STOCK + fsId, String.valueOf(totalStock));
        redisTemplate.opsForValue().set(KEY_ACTIVE + fsId, "1");
        log.info("秒杀库存预热完成: fsId={}, stock={}", fsId, totalStock);
        return true;
    }

    @Override
    public JSONObject grabFlashSale(String fsId, String uid) {
        JSONObject result = new JSONObject();
        List<String> keys = Arrays.asList(KEY_STOCK + fsId, KEY_USERS + fsId);
        Long stockRemain = redisTemplate.execute(stockLuaScript, keys, uid, "1");

        if (stockRemain == null) {
            result.put("code", -99);
            result.put("msg", "系统繁忙");
            return result;
        }

        if (stockRemain == -1) {
            result.put("code", -1);
            result.put("msg", "库存不足");
            return result;
        }
        if (stockRemain == -3) {
            result.put("code", -3);
            result.put("msg", "活动已结束");
            return result;
        }
        if (stockRemain == -4) {
            result.put("code", -4);
            result.put("msg", "已达到限购数量");
            return result;
        }

        // 扣库存成功 → MQ 异步下单
        JSONObject orderMsg = new JSONObject();
        orderMsg.put("fsId", fsId);
        orderMsg.put("uid", uid);
        orderMsg.put("timestamp", System.currentTimeMillis());

        if (rocketMQTemplate != null) {
            rocketMQTemplate.convertAndSend("FLASH_ORDER", orderMsg.toJSONString());
        }

        result.put("code", 0);
        result.put("msg", "抢购成功，正在排队处理");
        result.put("stockRemain", stockRemain);
        log.info("秒杀抢购成功: fsId={}, uid={}, remain={}", fsId, uid, stockRemain);
        return result;
    }

    @Override
    @Transactional
    public boolean rollbackStock(String fsId, String uid, int qty) {
        redisTemplate.opsForValue().increment(KEY_STOCK + fsId, qty);
        redisTemplate.opsForSet().remove(KEY_USERS + fsId, uid);
        redisTemplate.delete(KEY_BUY + fsId + ":" + uid);
        log.info("库存回滚: fsId={}, uid={}, qty={}", fsId, uid, qty);
        return true;
    }
}
