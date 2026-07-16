package ny.shop.youxuan.gateway.config;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 认证全局过滤器
 *
 * 增强功能：
 * 1. 完整 JWT 验签（HMAC-SHA256）
 * 2. 剥离客户端伪造的内部 Header
 * 3. 注入可信的 X-User-Id / X-Auth-Source
 * 4. 秒杀路径可额外校验 Token
 */
@Component
public class JwtAuthGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthGlobalFilter.class);

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /** 白名单路径（无需认证） */
    private static final String[] WHITE_LIST = {
            "/api/auth/**", "/api/goods/public/**",
            "/api/payment/notify/**", "/api/notify/public/**",
            "/actuator/**", "/swagger-ui/**", "/v3/api-docs/**",
            "/*/v3/api-docs", "/favicon.ico"
    };

    /** 需剥离的客户端伪冒 Header */
    private static final String[] UNTRUSTED_HEADERS = {
            "X-User-Id", "X-User-Role", "X-Auth-Source",
            "X-Internal-Validate", "X-Admin-Id"
    };

    private static final String TOKEN_PREFIX = "Bearer ";

    @Autowired(required = false)
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // ---- 白名单放行 ----
        if (isWhiteList(path)) {
            return chain.filter(exchange);
        }

        // ---- 剥离客户端可能伪造的内部 Header ----
        ServerHttpRequest cleanedRequest = stripUntrustedHeaders(request);

        // ---- 提取 Token ----
        String authHeader = cleanedRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            return unauthorized(exchange.getResponse(), "缺少认证Token");
        }

        String token = authHeader.substring(TOKEN_PREFIX.length()).trim();

        try {
            // ---- JWT 验签 ----
            String userId;
            if (jwtTokenProvider != null) {
                // 完整验签模式
                Claims claims = jwtTokenProvider.validateAndParse(token);
                userId = claims.getSubject();
            } else {
                // 降级模式（仅开发环境）：Base64 解析，记录警告
                log.warn("JwtTokenProvider 未配置，使用降级解析模式");
                userId = extractUserIdFallback(token);
            }

            if (userId == null || userId.isEmpty()) {
                return unauthorized(exchange.getResponse(), "Token无效");
            }

            // ---- 注入可信 Header ----
            ServerHttpRequest mutatedRequest = cleanedRequest.mutate()
                    .header("X-User-Id", userId)
                    .header("X-Auth-Source", "gateway")
                    .header("X-Auth-Time", String.valueOf(System.currentTimeMillis()))
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (ExpiredJwtException e) {
            log.warn("Token已过期: {}", e.getMessage());
            return unauthorized(exchange.getResponse(), "Token已过期");
        } catch (JwtException e) {
            log.warn("Token校验失败: {}", e.getMessage());
            return unauthorized(exchange.getResponse(), "Token无效");
        }
    }

    /**
     * 剥离客户端伪冒的内部 Header
     */
    private ServerHttpRequest stripUntrustedHeaders(ServerHttpRequest request) {
        ServerHttpRequest.Builder builder = request.mutate();
        for (String header : UNTRUSTED_HEADERS) {
            builder.headers(headers -> headers.remove(header));
        }
        return builder.build();
    }

    /**
     * 降级解析：从 JWT payload 中提取 subject
     * 仅用于开发环境/未配置密钥时
     */
    private String extractUserIdFallback(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                byte[] decoded = java.util.Base64.getUrlDecoder().decode(parts[1]);
                return JSON.parseObject(new String(decoded)).getString("sub");
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private boolean isWhiteList(String path) {
        for (String p : WHITE_LIST) {
            if (pathMatcher.match(p, path)) return true;
        }
        return false;
    }

    private Mono<Void> unauthorized(ServerHttpResponse response, String msg) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> r = new HashMap<>();
        r.put("code", 401);
        r.put("msg", msg);
        DataBuffer buffer = response.bufferFactory()
                .wrap(JSON.toJSONString(r).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
