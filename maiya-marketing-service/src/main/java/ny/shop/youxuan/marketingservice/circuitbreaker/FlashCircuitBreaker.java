package ny.shop.youxuan.marketingservice.circuitbreaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 秒杀专用熔断器
 *
 * 基于滑动窗口统计 DB 调用失败率
 * 失败率 > 阈值 → 熔断打开 → 快速失败 → 自动恢复
 *
 * 状态流转：
 *   CLOSED ── 失败率 >= 20% ──→ OPEN
 *   OPEN   ── 等待 30 秒 ────→ HALF_OPEN
 *   HALF_OPEN ── 试探成功 >= 3 ──→ CLOSED
 *   HALF_OPEN ── 试探失败 ───────→ OPEN
 */
@Component
public class FlashCircuitBreaker {

    private static final Logger log = LoggerFactory.getLogger(FlashCircuitBreaker.class);

    public enum State {
        CLOSED,     // 正常：请求放行
        OPEN,       // 熔断：快速失败
        HALF_OPEN   // 半开：放行试探请求
    }

    // -------- 配置 --------
    private static final int WINDOW_SIZE = 100;
    private static final double FAILURE_RATE_THRESHOLD = 0.20;
    private static final long RECOVERY_TIMEOUT_MS = 30_000;
    private static final int HALF_OPEN_MAX_REQUESTS = 5;
    private static final int HALF_OPEN_SUCCESS_THRESHOLD = 3;

    // 滑动窗口
    private final SlidingWindow window;

    // 当前状态
    private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);

    // 熔断打开时间
    private final AtomicLong openedAt = new AtomicLong(0);

    // 半开试探计数
    private final AtomicInteger halfOpenRequests = new AtomicInteger(0);
    private final AtomicInteger halfOpenSuccesses = new AtomicInteger(0);

    // 熔断次数统计
    private final AtomicInteger circuitBreakCount = new AtomicInteger(0);

    public FlashCircuitBreaker() {
        this.window = new SlidingWindow(WINDOW_SIZE);
    }

    /**
     * 判断请求是否允许通过
     * @return true=放行, false=熔断
     */
    public boolean tryAcquire() {
        State currentState = state.get();

        switch (currentState) {
            case CLOSED:
                return true;

            case OPEN:
                // 检查是否到达自动恢复时间
                if (System.currentTimeMillis() - openedAt.get() >= RECOVERY_TIMEOUT_MS) {
                    if (state.compareAndSet(State.OPEN, State.HALF_OPEN)) {
                        halfOpenRequests.set(0);
                        halfOpenSuccesses.set(0);
                        log.warn("熔断器转入 HALF_OPEN, 开始试探恢复");
                        return true;
                    }
                }
                circuitBreakCount.incrementAndGet();
                return false;

            case HALF_OPEN:
                // 半开：限制试探请求数
                if (halfOpenRequests.incrementAndGet() <= HALF_OPEN_MAX_REQUESTS) {
                    return true;
                }
                circuitBreakCount.incrementAndGet();
                return false;

            default:
                return true;
        }
    }

    /**
     * 记录成功
     */
    public void onSuccess() {
        State currentState = state.get();

        if (currentState == State.HALF_OPEN) {
            int successes = halfOpenSuccesses.incrementAndGet();
            if (successes >= HALF_OPEN_SUCCESS_THRESHOLD) {
                if (state.compareAndSet(State.HALF_OPEN, State.CLOSED)) {
                    window.reset();
                    log.warn("熔断器恢复 CLOSED, 服务恢复正常");
                }
            }
        }

        if (currentState == State.CLOSED) {
            window.record(true);
        }
    }

    /**
     * 记录失败
     */
    public void onFailure() {
        State currentState = state.get();

        switch (currentState) {
            case CLOSED:
                window.record(false);
                double failureRate = window.getFailureRate();
                if (failureRate >= FAILURE_RATE_THRESHOLD) {
                    if (state.compareAndSet(State.CLOSED, State.OPEN)) {
                        openedAt.set(System.currentTimeMillis());
                        log.warn("熔断器打开! 失败率={}%, 阈值={}%",
                                (int) (failureRate * 100), (int) (FAILURE_RATE_THRESHOLD * 100));
                    }
                }
                break;

            case HALF_OPEN:
                // 试探失败 → 立即回到 OPEN
                if (state.compareAndSet(State.HALF_OPEN, State.OPEN)) {
                    openedAt.set(System.currentTimeMillis());
                    log.warn("熔断试探失败, 回到 OPEN 状态");
                }
                break;

            case OPEN:
                break;
        }
    }

    /**
     * 手动重置熔断器（运维操作）
     */
    public void reset() {
        state.set(State.CLOSED);
        window.reset();
        log.warn("熔断器手动重置为 CLOSED");
    }

    public State getState() { return state.get(); }
    public double getFailureRate() { return window.getFailureRate(); }
    public int getCircuitBreakCount() { return circuitBreakCount.get(); }

    /**
     * 滑动窗口
     */
    static class SlidingWindow {
        private final boolean[] results;
        private int index = 0;
        private int count = 0;

        SlidingWindow(int size) {
            this.results = new boolean[size];
        }

        synchronized void record(boolean success) {
            results[index % results.length] = success;
            index++;
            if (count < results.length) count++;
        }

        synchronized double getFailureRate() {
            if (count == 0) return 0.0;
            int failures = 0;
            int start = Math.max(0, index - results.length);
            for (int i = start; i < index; i++) {
                if (!results[i % results.length]) failures++;
            }
            return (double) failures / count;
        }

        synchronized void reset() {
            Arrays.fill(results, false);
            index = 0;
            count = 0;
        }
    }
}
