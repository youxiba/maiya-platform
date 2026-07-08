package ny.shop.youxuan.gateway.config;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger("ACCESS_LOG");
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest(); long start = System.currentTimeMillis();
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            JSONObject entry = new JSONObject();
            entry.put("time", LocalDateTime.now().format(DTF));
            entry.put("method", request.getMethod().name());
            entry.put("path", request.getURI().getPath());
            entry.put("status", exchange.getResponse().getStatusCode() != null ? exchange.getResponse().getStatusCode().value() : 0);
            entry.put("cost_ms", System.currentTimeMillis() - start);
            entry.put("ip", request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : "");
            if (System.currentTimeMillis() - start > 3000) { entry.put("slow_query", true); log.warn("SLOW: {}", entry.toJSONString()); }
            log.info(entry.toJSONString());
        }));
    }
    @Override public int getOrder() { return -50; }
}