package ny.shop.youxuan.marketingservice.mq;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ny.shop.youxuan.marketingservice.dto.FlashOrderMessage;
import ny.shop.youxuan.marketingservice.service.batch.BatchOrderService;
import ny.shop.youxuan.marketingservice.service.batch.FlashOrderWriteResult;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 秒杀订单批量消费者
 *
 * 核心设计：
 * 1. 攒批：每 100 条 或 每 500ms 刷一次
 * 2. 批量 INSERT IGNORE（唯一键防重）
 * 3. 批量扣库存（乐观锁）
 * 4. 失败降级单条写入
 */
@Component
@RocketMQMessageListener(
        topic = "FLASH_ORDER",
        consumerGroup = "consumer-flash-order",
        consumeMode = ConsumeMode.ORDERLY,
        consumeThreadMax = 32,
        consumeThreadNumber = 16
)
public class FlashBatchConsumer implements RocketMQListener<MessageExt> {

    private static final Logger log = LoggerFactory.getLogger(FlashBatchConsumer.class);

    @Autowired
    private BatchOrderService batchOrderService;

    // 攒批参数
    private static final int BATCH_SIZE = 100;
    private static final long FLUSH_INTERVAL_MS = 500;
    private static final int MAX_BUFFER_SIZE = 10000;

    // 缓冲区（每个 fsId 独立缓冲区，顺序消费保证同 fsId 进同一线程）
    private final ConcurrentHashMap<String, BatchBuffer> buffers = new ConcurrentHashMap<>();

    // 定时刷新线程
    private final ScheduledExecutorService flushScheduler = Executors.newSingleThreadScheduledExecutor();

    // 缓冲区积压告警
    private final AtomicInteger totalBuffered = new AtomicInteger(0);

    @jakarta.annotation.PostConstruct
    public void init() {
        flushScheduler.scheduleAtFixedRate(
                this::flushAll, FLUSH_INTERVAL_MS, FLUSH_INTERVAL_MS, TimeUnit.MILLISECONDS);
        log.info("FlashBatchConsumer 初始化完成: batchSize={}, flushInterval={}ms",
                BATCH_SIZE, FLUSH_INTERVAL_MS);
    }

    @Override
    public void onMessage(MessageExt msg) {
        try {
            String body = new String(msg.getBody(), StandardCharsets.UTF_8);
            FlashOrderMessage message = JSON.parseObject(body, FlashOrderMessage.class);

            if (message.getFsId() == null || message.getUid() == null) {
                log.warn("消息参数不完整: {}", body);
                return;
            }

            // 写入缓冲区
            BatchBuffer buffer = buffers.computeIfAbsent(
                    message.getFsId(), k -> new BatchBuffer(BATCH_SIZE));

            synchronized (buffer) {
                buffer.add(message);
                totalBuffered.incrementAndGet();
            }

            // 达到批量阈值立即刷出
            if (buffer.size() >= BATCH_SIZE) {
                flushBuffer(message.getFsId(), buffer);
            }

            // 缓冲区总积压告警
            if (totalBuffered.get() > MAX_BUFFER_SIZE) {
                log.warn("缓冲区积压严重: total={}, fsIds={}",
                        totalBuffered.get(), buffers.size());
            }

        } catch (Exception e) {
            log.error("消息解析失败, msgId={}, body={}",
                    msg.getMsgId(), new String(msg.getBody()), e);
        }
    }

    /**
     * 定时刷出所有过期缓冲区
     */
    private void flushAll() {
        buffers.forEach((fsId, buffer) -> {
            synchronized (buffer) {
                if (!buffer.isEmpty() && buffer.isExpired(FLUSH_INTERVAL_MS)) {
                    flushBuffer(fsId, buffer);
                }
            }
        });
    }

    /**
     * 刷出单个缓冲区
     */
    private void flushBuffer(String fsId, BatchBuffer buffer) {
        List<FlashOrderMessage> batch;
        synchronized (buffer) {
            batch = buffer.drain();
        }

        if (batch.isEmpty()) return;

        totalBuffered.addAndGet(-batch.size());
        long start = System.currentTimeMillis();

        try {
            FlashOrderWriteResult result = batchOrderService.batchCreateOrders(batch);
            log.info("批量写入: fsId={}, batch={}, succ={}, dup={}, cost={}ms",
                    fsId, batch.size(), result.getSuccessCount(),
                    result.getDuplicateCount(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("批量写入失败, 降级单条: fsId={}, batch={}, error={}",
                    fsId, batch.size(), e.getMessage());
            // 降级：逐条写入
            for (FlashOrderMessage msg : batch) {
                try {
                    batchOrderService.singleCreateOrder(msg);
                } catch (Exception e2) {
                    log.error("单条写入失败, 丢失: requestId={}", msg.getRequestId(), e2);
                }
            }
        }
    }

    /**
     * 缓冲区
     */
    static class BatchBuffer {
        private final List<FlashOrderMessage> messages;
        private final int maxSize;
        private long lastFlushTime;

        BatchBuffer(int maxSize) {
            this.messages = new ArrayList<>();
            this.maxSize = maxSize;
            this.lastFlushTime = System.currentTimeMillis();
        }

        synchronized void add(FlashOrderMessage msg) {
            messages.add(msg);
        }

        synchronized List<FlashOrderMessage> drain() {
            List<FlashOrderMessage> drained = new ArrayList<>(messages);
            messages.clear();
            lastFlushTime = System.currentTimeMillis();
            return drained;
        }

        synchronized int size() { return messages.size(); }
        synchronized boolean isEmpty() { return messages.isEmpty(); }

        boolean isExpired(long intervalMs) {
            return (System.currentTimeMillis() - lastFlushTime) >= intervalMs;
        }
    }
}
