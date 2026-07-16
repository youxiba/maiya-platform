package ny.shop.youxuan.common.util;

/**
 * 分布式 ID 生成器（雪花算法）
 *
 * 生产分布式唯一 ID：
 * - 1位符号位 + 41位时间戳 + 10位机器ID + 12位序列号
 * - 单机每秒可生成 409.6 万个 ID
 * - 按时间有序递增
 */
public class IdGenerator {

    /** 开始时间戳（2024-01-01） */
    private static final long START_EPOCH = 1704067200000L;

    /** 机器 ID 位数 */
    private static final long WORKER_ID_BITS = 10L;

    /** 序列号位数 */
    private static final long SEQUENCE_BITS = 12L;

    /** 最大机器 ID */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /** 序列号掩码 */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /** 机器 ID 左移位数 */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /** 时间戳左移位数 */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    private final long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    /**
     * @param workerId 机器 ID（0 ~ 1023）
     */
    public IdGenerator(long workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(
                String.format("Worker ID must be between 0 and %d", MAX_WORKER_ID));
        }
        this.workerId = workerId;
    }

    /**
     * 生成下一个唯一 ID
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // 同一毫秒内序列号用完，等待下一毫秒
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - START_EPOCH) << TIMESTAMP_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 生成字符串格式的 ID（纯数字）
     */
    public String nextIdStr() {
        return String.valueOf(nextId());
    }

    /**
     * 生成短 ID（36进制，更短）
     */
    public String nextShortId() {
        return Long.toString(nextId(), 36).toUpperCase();
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
