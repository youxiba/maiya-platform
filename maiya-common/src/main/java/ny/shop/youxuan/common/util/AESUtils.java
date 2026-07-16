package ny.shop.youxuan.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.util.Base64;

/**
 * AES 加密解密工具类
 *
 * 支持 AES/CBC/PKCS7Padding 模式。
 * 用于微信小程序用户信息解密、敏感数据加解密等场景。
 */
public class AESUtils {

    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    private AESUtils() {}

    /**
     * AES/CBC/PKCS7Padding 解密
     *
     * @param encryptedData 加密数据（Base64）
     * @param keyBytes      密钥
     * @param ivBytes       IV 向量
     * @return 解密后的原文
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] keyBytes, byte[] ivBytes) throws Exception {
        Key key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        // PKCS7Padding 需要 BouncyCastle 支持
        // 如果是 JDK 原生实现不支持 PKCS7Padding，可用 PKCS5Padding 替代
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, generateIV(ivBytes));
        } catch (Exception e) {
            // 降级使用 PKCS5Padding
            Cipher alt = Cipher.getInstance("AES/CBC/PKCS5Padding");
            alt.init(Cipher.DECRYPT_MODE, key, generateIV(ivBytes));
            return alt.doFinal(encryptedData);
        }

        return cipher.doFinal(encryptedData);
    }

    /**
     * AES 加密
     *
     * @param data     明文
     * @param keyBytes 密钥
     * @param ivBytes  IV 向量
     * @return 密文
     */
    public static byte[] encrypt(byte[] data, byte[] keyBytes, byte[] ivBytes) throws Exception {
        Key key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, generateIV(ivBytes));
        return cipher.doFinal(data);
    }

    /**
     * 微信小程序手机号解密
     *
     * @param encryptedData 加密数据（Base64 编码）
     * @param sessionKey    会话密钥（Base64 编码）
     * @param iv            IV 向量（Base64 编码）
     * @return 解密后的 JSON 字符串
     */
    public static String wxDecrypt(String encryptedData, String sessionKey, String iv) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] keyBytes = Base64.getDecoder().decode(sessionKey);
        byte[] ivBytes = Base64.getDecoder().decode(iv);
        byte[] decrypted = decrypt(encryptedBytes, keyBytes, ivBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
        params.init(new IvParameterSpec(iv));
        return params;
    }
}
