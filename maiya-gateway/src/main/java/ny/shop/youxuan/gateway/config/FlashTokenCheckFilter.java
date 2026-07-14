package ny.shop.youxuan.gateway.config;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
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
 * 秒杀 Token 校验网关过滤器
 *
 * 在 JWT 认证过滤器之后执行，校验秒杀抢购接口的 Flash-Token
 * 防止请求穿透到秒杀服务
 *
 * 执行顺序：在 JwtAuthGlobalFilter(-100) 之后，Sentinel 之前
 */
@Component
public class FlashTokenCheckFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(FlashTokenCheckFilter.class);

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /** 需要 Token 校验的路径 */
    private static final String[] TOKEN_REQUIRED_PATHS = {
            "/api/flash/grab"
    };

    private static final String KEY_TOKEN_USER = "flash:token_user:";
    private static final String KEY_TOKEN_USED = "flash:token_used:";

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 非秒杀抢购路径，跳过
        if (!requiresToken(path)) {
            return chain.filter(exchange);
        }

        // 只允许 POST
        if (!HttpMethod.POST.matches(request.getMethod().name())) {
            return forbidden(exchange.getResponse(), "请求方法不允许");
        }

        // 如果没有配置 Redis，跳过 Token 校验（降级模式）
        if (redisTemplate == null) {
            log.warn("Redis 未配置，跳过秒杀 Token 校验");
            return chain.filter(exchange);
        }

        // 从 Header 获取秒杀 Token
        String flashToken = request.getHeaders().getFirst("X-Flash-Token");
        if (flashToken == null || flashToken.isEmpty()) {
            log.warn("缺少秒杀令牌: path={}", path);
            return forbidden(exchange.getResponse(), "缺少秒杀令牌");
        }

        // 获取用户 ID（由 JwtAuthGlobalFilter 注入）
        String userId = request.getHeaders().getFirst("X-User-Id");
        if (userId == null || userId.isEmpty()) {
            return unauthorized(exchange.getResponse(), "未认证用户");
        }

        // 获取 fsId
        String fsId = request.getQueryParams().getFirst("fsId");

        // 校验 Token
        boolean valid = verifyToken(fsId, userId, flashToken);
        if (!valid) {
            log.warn("秒杀令牌无效: uid={}, fsId={}", userId, fsId);
            return forbidden(exchange.getResponse(), "秒杀令牌无效或已使用");
        }

        // 标记 Token 已校验
        exchange.getAttributes().put("flashTokenVerified", true);
        exchange.getAttributes().put("flashFsId", fsId);

        return chain.filter(exchange);
    }

    /**
     * 校验 Token（Redis 中查询是否已发放给该用户且未使用）
     */
    private boolean verifyToken(String fsId, String userId, String flashToken) {
        if (fsId == null || userId == null || flashToken == null) return false;

        try {
            // 1. 检查是否已使用
            Boolean used = redisTemplate.opsForSet().isMember(
                    KEY_TOKEN_USED + fsId, flashToken);
            if (Boolean.TRUE.equals(used)) return false;

            // 2. 检查是否发放给该用户
            String userKey = KEY_TOKEN_USER + userId + ":" + fsId;
            String stored = redisTemplate.opsForValue().get(userKey);
            return flashToken.equals(stored);

        } catch (Exception e) {
            log.error("校验秒杀Token异常: {}", e.getMessage());
            // Redis 异常时降级放行，避免影响正常流程
            return true;
        }
    }

    private boolean requiresToken(String path) {
        for (String p : TOKEN_REQUIRED_PATHS) {
            if (pathMatcher.match(p, path)) return true;
        }
        return false;
    }

    private Mono<Void> forbidden(ServerHttpResponse response, String msg) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> r = new HashMap<>();
        r.put("code", 403);
        r.put("msg", msg);
        DataBuffer buffer = response.bufferFactory()
                .wrap(JSON.toJSONString(r).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
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
        return -50; // 在 JwtAuthGlobalFilter(-100) 之后执行
    }
}
