package ny.shop.youxuan.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal 运算工具类
 *
 * 提供精确的金额计算、比例分配等财务相关运算方法。
 */
public class BigDecimalUtils {

    private static final int DEFAULT_SCALE = 2;

    private BigDecimalUtils() {}

    /** 加法 */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        if (a == null) a = BigDecimal.ZERO;
        if (b == null) b = BigDecimal.ZERO;
        return a.add(b);
    }

    /** 减法 */
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        if (a == null) a = BigDecimal.ZERO;
        if (b == null) b = BigDecimal.ZERO;
        return a.subtract(b);
    }

    /** 乘法 */
    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) return BigDecimal.ZERO;
        return a.multiply(b);
    }

    /** 除法（默认精度 2，向下取整） */
    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return divide(a, b, DEFAULT_SCALE);
    }

    /** 除法（指定精度） */
    public static BigDecimal divide(BigDecimal a, BigDecimal b, int scale) {
        if (a == null || b == null || b.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return a.divide(b, scale, RoundingMode.DOWN);
    }

    /** 计算比例（a * ratio / 100） */
    public static BigDecimal percentage(BigDecimal a, BigDecimal ratio) {
        if (a == null || ratio == null) return BigDecimal.ZERO;
        return a.multiply(ratio).divide(new BigDecimal(100), DEFAULT_SCALE, RoundingMode.DOWN);
    }

    /** 保留 2 位小数（向下取整） */
    public static BigDecimal scale(BigDecimal value) {
        return scale(value, DEFAULT_SCALE);
    }

    /** 保留指定位数小数（向下取整） */
    public static BigDecimal scale(BigDecimal value, int scale) {
        if (value == null) return BigDecimal.ZERO;
        return value.setScale(scale, RoundingMode.DOWN);
    }

    /** 金额转换为分（元→分） */
    public static long toCents(BigDecimal yuan) {
        if (yuan == null) return 0;
        return yuan.multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).longValue();
    }

    /** 分转换为元（分→元） */
    public static BigDecimal toYuan(long cents) {
        return new BigDecimal(cents).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
    }

    /** 判断是否大于 0 */
    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }

    /** 判断是否等于 0 */
    public static boolean isZero(BigDecimal value) {
        return value == null || value.compareTo(BigDecimal.ZERO) == 0;
    }
}
