package ny.shop.youxuan.marketingservice.service.batch;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ny.shop.youxuan.marketingservice.dto.FlashOrderMessage;
import ny.shop.youxuan.marketingservice.entity.FlashOrder;
import ny.shop.youxuan.marketingservice.mapper.FlashOrderMapper;
import ny.shop.youxuan.marketingservice.mapper.FlashSaleMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 批量订单写入服务
 *
 * 一个事务内完成：
 *   1. 批量 INSERT flash_order（唯一键防重）
 *   2. 批量 UPDATE flash_sale.sold_stock（乐观锁）
 *
 * 唯一键冲突 = 重复消息，静默忽略
 */
@Service
public class BatchOrderService {

    private static final Logger log = LoggerFactory.getLogger(BatchOrderService.class);

    @Autowired
    private FlashOrderMapper flashOrderMapper;

    @Autowired
    private FlashSaleMapper flashSaleMapper;

    /**
     * 批量创建订单（事务内）
     */
    @Transactional(rollbackFor = Exception.class)
    public FlashOrderWriteResult batchCreateOrders(List<FlashOrderMessage> messages) {
        FlashOrderWriteResult result = new FlashOrderWriteResult();
        if (messages == null || messages.isEmpty()) return result;

        // ---- 1. 构建订单实体 ----
        List<FlashOrder> orders = messages.stream()
                .map(this::buildOrder)
                .collect(Collectors.toList());

        // ---- 2. 统计每个活动需扣减库存 ----
        Map<String, Integer> fsIdQtyMap = new HashMap<>();
        for (FlashOrderMessage msg : messages) {
            fsIdQtyMap.merge(msg.getFsId(), 1, Integer::sum);
        }

        // ---- 3. 批量 INSERT（IGNORE 忽略重复） ----
        int inserted = flashOrderMapper.batchInsertIgnore(orders);
        result.setSuccessCount(inserted);
        result.setDuplicateCount(messages.size() - inserted);

        if (inserted == 0) {
            // 全部是重复消息
            return result;
        }

        // ---- 4. 批量扣减真实库存 ----
        for (Map.Entry<String, Integer> entry : fsIdQtyMap.entrySet()) {
            int affected = flashSaleMapper.deductStock(entry.getKey(), entry.getValue());
            if (affected == 0) {
                log.error("真实库存扣减失败(可能不足): fsId={}, need={}", entry.getKey(), entry.getValue());
            }
        }

        log.info("批量写入完成: batch={}, success={}, duplicate={}",
                messages.size(), inserted, messages.size() - inserted);
        return result;
    }

    /**
     * 单条创建订单（批量失败后降级）
     */
    @Transactional(rollbackFor = Exception.class)
    public void singleCreateOrder(FlashOrderMessage msg) {
        // 1. 唯一键防重插入
        try {
            flashOrderMapper.insertWithUniqueCheck(buildOrder(msg));
        } catch (DuplicateKeyException e) {
            log.debug("重复订单, 忽略: requestId={}", msg.getRequestId());
            return;
        }

        // 2. 扣库存
        int affected = flashSaleMapper.deductStock(msg.getFsId(), 1);
        if (affected == 0) {
            log.error("单条扣库存失败: fsId={}", msg.getFsId());
        }
    }

    /**
     * FlashOrderMessage → FlashOrder
     */
    private FlashOrder buildOrder(FlashOrderMessage msg) {
        FlashOrder order = new FlashOrder();
        order.setRequestId(msg.getRequestId());
        order.setOrderId(generateOrderId(msg.getFsId(), msg.getUid()));
        order.setFsId(msg.getFsId());
        order.setUid(msg.getUid());
        order.setGoodsInfoId(msg.getFsId());
        order.setFlashPrice(BigDecimal.valueOf(0.01));
        order.setQty(1);
        order.setOrderStatus(0);    // 待支付
        order.setCreateTime(System.currentTimeMillis());
        return order;
    }

    private String generateOrderId(String fsId, String uid) {
        String ts = String.valueOf(System.currentTimeMillis());
        String uidSuffix = uid.length() > 4 ? uid.substring(uid.length() - 4) : uid;
        String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        return "F" + ts + uidSuffix + rand;
    }
}
