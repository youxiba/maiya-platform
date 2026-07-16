package ny.shop.youxuan.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 加密工具类
 */
public class MD5Utils {

    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    private MD5Utils() {}

    /**
     * MD5 加密（小写十六进制）
     */
    public static String encode(String source) {
        if (source == null) return null;
        return encode(source.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * MD5 加密（带盐）
     */
    public static String encodeWithSalt(String source, String salt) {
        if (source == null) return null;
        return encode(source + salt);
    }

    /**
     * MD5 加密（字节数组 → 十六进制）
     */
    public static String encode(byte[] source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder(32);
            for (byte b : digest) {
                sb.append(HEX_DIGITS[(b >> 4) & 0x0f]);
                sb.append(HEX_DIGITS[b & 0x0f]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    /**
     * MD5 验证
     */
    public static boolean verify(String source, String md5) {
        if (source == null || md5 == null) return false;
        return encode(source).equalsIgnoreCase(md5);
    }
}
