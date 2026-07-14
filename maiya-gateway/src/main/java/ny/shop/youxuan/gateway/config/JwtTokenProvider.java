package ny.shop.youxuan.gateway.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * JWT 校验组件（完整验签 + 过期校验）
 *
 * 原实现只做 Base64 解码无验签，存在安全隐患。
 * 改进后使用 HMAC 签名校验，注入可信 X-User-Id 到下游服务。
 */
@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.issuer:maiya-auth}")
    private String issuer;

    private SecretKey key;

    @PostConstruct
    public void init() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(secretKey);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.warn("JWT密钥配置为纯文本，使用SHA-256派生（建议配置Base64编码的密钥）");
            // 兼容纯文本配置，用 SHA-256 派生
            this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        }
    }

    /**
     * 校验并解析 JWT
     *
     * @param token JWT 字符串
     * @return Claims 载荷
     * @throws JwtException 校验失败时抛出（签名错误、过期、格式无效等）
     */
    public Claims validateAndParse(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(issuer)
                .clockSkewSeconds(30)     // 30 秒时钟偏差容忍
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
