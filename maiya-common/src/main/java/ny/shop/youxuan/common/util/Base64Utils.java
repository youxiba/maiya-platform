package ny.shop.youxuan.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64 编解码工具类
 */
public class Base64Utils {

    private Base64Utils() {}

    /**
     * Base64 编码
     */
    public static String encode(byte[] data) {
        if (data == null) return null;
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Base64 编码（字符串输入）
     */
    public static String encode(String text) {
        if (text == null) return null;
        return encode(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64 解码
     */
    public static byte[] decode(String base64) {
        if (base64 == null) return null;
        return Base64.getDecoder().decode(base64);
    }

    /**
     * Base64 解码为字符串
     */
    public static String decodeToString(String base64) {
        byte[] data = decode(base64);
        if (data == null) return null;
        return new String(data, StandardCharsets.UTF_8);
    }

    /**
     * URL 安全的 Base64 编码（替换 +/= 为 -/_）
     */
    public static String encodeUrlSafe(byte[] data) {
        if (data == null) return null;
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    /**
     * URL 安全的 Base64 解码
     */
    public static byte[] decodeUrlSafe(String base64) {
        if (base64 == null) return null;
        return Base64.getUrlDecoder().decode(base64);
    }
}
