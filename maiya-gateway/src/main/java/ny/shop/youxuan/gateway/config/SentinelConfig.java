package ny.shop.youxuan.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.HashMap;

@Configuration
public class SentinelConfig {
    @Bean
    public BlockRequestHandler blockRequestHandler() {
        return (e, t) -> {
            HashMap r = new HashMap();
            r.put("code", 429);
            r.put("message", "请求频繁");
            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(com.alibaba.fastjson.JSONObject.toJSONString(r)), String.class);
        };
    }
}