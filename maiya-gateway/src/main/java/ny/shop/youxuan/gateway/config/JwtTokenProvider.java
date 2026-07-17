package ny.shop.youxuan.gateway.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * JWT 校验组件（完整验签 + 过期校验）
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.issuer:maiya-auth}")
    private String issuer;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
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
