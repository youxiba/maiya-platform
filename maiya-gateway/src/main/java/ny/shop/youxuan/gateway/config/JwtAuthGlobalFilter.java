package ny.shop.youxuan.gateway.config;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Component
public class JwtAuthGlobalFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthGlobalFilter.class);
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final String[] WHITE_LIST = { "/api/auth/**", "/api/goods/public/**", "/api/payment/notify/**",
            "/api/notify/public/**", "/actuator/**", "/swagger-ui/**", "/v3/api-docs/**", "/favicon.ico" };
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if (isWhiteList(path)) {
            return chain.filter(exchange);
        }
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            return unauthorized(exchange.getResponse(), "缺少认证Token");
        }
        String token = authHeader.substring(TOKEN_PREFIX.length()).trim();
        if (token.isEmpty() || token.split("\\.").length != 3) {
            return unauthorized(exchange.getResponse(), "Token格式无效");
        }
        ServerHttpRequest mutatedRequest = request.mutate().header("X-User-Token", token)
                .header("X-User-Id", extractUserIdFromToken(token)).build();
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private boolean isWhiteList(String path) {
        for (String p : WHITE_LIST) {
            if (pathMatcher.match(p, path))
                return true;
        }
        return false;
    }

    private String extractUserIdFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                byte[] decoded = java.util.Base64.getUrlDecoder().decode(parts[1]);
                return JSON.parseObject(new String(decoded)).getString("sub");
            }
        } catch (Exception e) {
        }
        return "";
    }

    private Mono<Void> unauthorized(ServerHttpResponse response, String msg) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> r = new HashMap<>();
        r.put("code", 401);
        r.put("message", msg);
        DataBuffer buffer = response.bufferFactory().wrap(JSON.toJSONString(r).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}