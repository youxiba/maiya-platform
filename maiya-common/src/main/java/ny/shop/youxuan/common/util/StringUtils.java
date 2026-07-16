package ny.shop.youxuan.common.util;

/**
 * 字符串工具类
 *
 * 提供判空、脱敏、截取等字符串操作方法。
 * 替代原 com.zeroing.sdk:utils-java 中的字符串功能。
 */
public class StringUtils {

    private StringUtils() {}

    /** 判断字符串是否为空白 */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /** 判断字符串是否不为空白 */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    /** 判断字符串是否为空 */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /** 判断字符串是否不为空 */
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    /** 字符串截取 */
    public static String truncate(String str, int maxLength) {
        if (str == null) return null;
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength);
    }

    /**
     * 手机号脱敏：138****1234
     */
    public static String maskMobile(String mobile) {
        if (isBlank(mobile) || mobile.length() < 7) return mobile;
        return mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4);
    }

    /**
     * 身份证脱敏：110***********1234
     */
    public static String maskIdCard(String idCard) {
        if (isBlank(idCard) || idCard.length() < 10) return idCard;
        return idCard.substring(0, 3) + "***********" + idCard.substring(idCard.length() - 4);
    }

    /** 默认值：为空时返回默认值 */
    public static String defaultIfBlank(String str, String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    /** 左补字符 */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) return null;
        int pads = size - str.length();
        if (pads <= 0) return str;
        StringBuilder sb = new StringBuilder(size);
        sb.append(String.valueOf(padChar).repeat(pads));
        sb.append(str);
        return sb.toString();
    }
}
