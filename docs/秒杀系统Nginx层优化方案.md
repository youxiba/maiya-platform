# 秒杀系统 Nginx 层优化方案

> 基于 maiya-platform 秒杀系统架构的 Nginx 反向代理层优化

## 架构位置

```
用户 → DNS → CDN
                ↘
              Nginx（反向代理层） → 网关 → 秒杀服务 → Redis Lua → MQ
                ↑
          本次优化重点
```

Nginx 位于 CDN 回源之后、网关之前，是整个系统的**最后一道防线**——所有到达源站的流量都必须经过 Nginx。

---

## 一、请求限流（最核心）

### 1.1 漏桶限流 — 削峰填谷

```nginx
# nginx-flash-limit.conf

# -------- 限流区域定义 ----------

# 用户级别限流（基于 Cookie 或 Header 中的 uid）
# 每秒最多处理 1000 个秒杀请求，突发 200
limit_req_zone $http_x_user_id zone=flash_user:10m rate=1000r/s;

# IP 级别限流
limit_req_zone $binary_remote_addr zone=flash_ip:10m rate=3r/s;

# URI 级别限流（秒杀抢购总入口）
limit_req_zone $request_uri zone=flash_grab:10m rate=5000r/s;

# -------- 连接数限制 ----------

# 每个 IP 最多同时建立 10 个连接
limit_conn_zone $binary_remote_addr zone=perip:10m;

# 每台 Nginx 到后端的最大连接数
limit_conn_zone $upstream_addr zone=upstream_conn:10m;
```

### 1.2 秒杀 API 限流配置

```nginx
server {
    listen 443 ssl;
    server_name flash.maiya.youxuan.shop;

    # -------- 静态资源（不限流） ----------
    location /static/ {
        alias /data/flash-static/;
        expires 30d;
        add_header Cache-Control "public, immutable";
        access_log off;
    }

    # -------- 秒杀抢购（严格限流） ----------
    location /api/flash/grab {
        # IP 限流
        limit_req zone=flash_ip burst=5 nodelay;
        # 用户限流（基于 uid）
        limit_req zone=flash_user burst=10;
        # 总入口限流
        limit_req zone=flash_grab burst=1000 nodelay;
        # 连接数限制
        limit_conn perip 5;

        # 限流拒绝时的状态码
        limit_req_status 429;
        limit_conn_status 503;

        # 限流时返回自定义 JSON，而不是默认页面
        error_page 429 = @rate_limit_response;
        error_page 503 = @rate_limit_response;

        proxy_pass http://flash_backend;
        include proxy_params_flash.conf;
    }

    # -------- 秒杀活动信息（温和限流） ----------
    location /api/flash/info {
        # 查询接口放宽松一些
        limit_req zone=flash_ip burst=20 nodelay;

        proxy_pass http://flash_backend;
        include proxy_params_flash.conf;
    }

    # -------- 限流响应 ----------
    location @rate_limit_response {
        default_type application/json;
        return 429 '{"code":-6,"msg":"请求过于频繁，请稍后重试"}';
    }
}
```

### 1.3 多级限流参数参考

| 限流维度 | 位置 | 速率 | 突发 | 说明 |
|---|---|---|---|---|
| 总入口 | URI | 5000 r/s | 1000 | 网关承载上限 |
| 用户 | Header uid | 1 r/s | 5 | 防止手抖连点 |
| IP | 远程地址 | 3 r/s | 5 | 防止脚本刷单 |
| 连接数 | IP | 5 并发 | — | 防止连接耗尽 |

---

## 二、上游连接优化

### 2.1 长连接配置

秒杀请求的特点是：短小、高频、突发。复用连接能大幅降低握手开销。

```nginx
# proxy_params_flash.conf
proxy_http_version 1.1;

# 开启到后端的长连接
proxy_set_header Connection "";

# 连接池配置
upstream flash_backend {
    # 网关实例列表
    server 192.168.1.10:8080 weight=5;
    server 192.168.1.11:8080 weight=5;
    server 192.168.1.12:8080 weight=5;

    # 长连接池
    keepalive 128;               # 每个 worker 保持 128 个空闲连接
    keepalive_requests 10000;    # 单个连接最多处理 1 万次请求后关闭
    keepalive_timeout 30s;       # 空闲连接超过 30s 关闭

    # 健康检查
    health_check interval=3s fails=3 passes=2;
}

# 超时配置（秒杀请求必须短平快）
proxy_connect_timeout 2s;        # 连接后端超时
proxy_send_timeout 3s;           # 发送请求超时
proxy_read_timeout 5s;           # 等待响应超时
```

### 2.2 缓冲优化

秒杀场景吞吐量大，缓冲配置需要权衡内存与性能：

```nginx
# proxy_params_flash.conf（续）

# 请求体缓冲 — 秒杀请求体很小，不做缓冲，减少延迟
proxy_request_buffering off;

# 响应缓冲 — 开启缓冲，减少后端写阻塞
proxy_buffering on;
proxy_buffer_size 4k;
proxy_buffers 8 4k;
proxy_busy_buffers_size 8k;

# 后端响应体上限（秒杀响应很小，限制防止异常）
proxy_max_temp_file_size 0;      # 禁止写入临时文件（全内存）
proxy_temp_file_write_size 0;
```

---

## 三、SSL/TLS 优化

HTTPS 握手在秒杀场景下是显著的性能开销。开启 SSL 会话复用：

```nginx
server {
    listen 443 ssl;
    server_name flash.maiya.youxuan.shop;

    # 证书配置
    ssl_certificate     /etc/nginx/certs/flash.pem;
    ssl_certificate_key /etc/nginx/certs/flash-key.pem;

    # 会话复用
    ssl_session_cache shared:flash_ssl:50m;      # 50MB 会话缓存，约 50 万条
    ssl_session_timeout 10m;                      # 会话超时 10 分钟
    ssl_session_tickets on;
    ssl_session_ticket_key /etc/nginx/ssl_ticket.key;  # 多节点共享 key

    # 协议与加密套件（只选最快的前 4 个）
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers 'ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305';
    ssl_prefer_server_ciphers on;

    # OCSP Stapling（减少客户端验证证书的延迟）
    ssl_stapling on;
    ssl_stapling_verify on;
    resolver 8.8.8.8 223.5.5.5 valid=300s;
}
```

---

## 四、请求预处理（Lua 扩展）

Nginx 配合 Lua（OpenResty）可以在反向代理层做更灵活的预处理：

```nginx
# OpenResty 版本：在 access 阶段做预处理
# 需要安装 lua-nginx-module
```

### 4.1 Lua 限流与校验

```nginx
server {
    location /api/flash/grab {
        access_by_lua_block {
            -- 获取参数
            local body = ngx.req.get_body_data()
            if not body then
                ngx.exit(ngx.HTTP_BAD_REQUEST)
                return
            end

            local cjson = require("cjson")
            local ok, data = pcall(cjson.decode, body)
            if not ok or not data.fsId or not data.uid then
                ngx.status = 400
                ngx.say('{"code":-4,"msg":"参数不完整"}')
                ngx.exit(ngx.HTTP_OK)
                return
            end

            -- 共享内存计数器（跨 worker 限流）
            local limit = ngx.shared.flash_limit
            local key = "user:" .. data.uid
            local current, err = limit:incr(key, 1, 0, 1)  -- incr, init=0, ttl=1s
            if current and current > 3 then
                ngx.status = 429
                ngx.say('{"code":-6,"msg":"操作太快了"}')
                ngx.exit(ngx.HTTP_OK)
                return
            end

            -- 通过，设置头部给后端
            ngx.req.set_header("X-Internal-Validate", "1")
        }

        proxy_pass http://flash_backend;
    }
}
```

### 4.2 请求重写与参数校验

```nginx
server {
    location /api/flash/grab {
        rewrite_by_lua_block {
            -- 清理不必要的参数，减小请求体
            local cjson = require("cjson")
            ngx.req.read_body()
            local body = ngx.req.get_body_data()
            if body then
                local data = cjson.decode(body)
                -- 只保留必要字段
                local cleaned = { fsId = data.fsId, uid = data.uid }
                ngx.req.set_body_data(cjson.encode(cleaned))
            end
        }

        proxy_pass http://flash_backend;
    }
}
```

---

## 五、日志与监控

### 5.1 秒杀专用日志格式

```nginx
# 秒杀日志格式（包含关键字段）
log_format flash_log '$remote_addr - $http_x_user_id [$time_local] '
                     '"$request" $status $body_bytes_sent '
                     '$request_time $upstream_response_time '
                     '$upstream_addr $upstream_connect_time '
                     '"$http_x_forwarded_for"';

server {
    # 秒杀 API 独立日志
    location /api/flash/ {
        access_log /var/log/nginx/flash-access.log flash_log buffer=32k flush=5s;
        # 错误日志级别调低，避免刷盘
        error_log /var/log/nginx/flash-error.log warn;
    }
}
```

### 5.2 实时状态监控

```nginx
# 开启 Nginx 状态页面（用于接入 Prometheus 或 Zabbix）
server {
    listen 127.0.0.1:81;
    location /nginx_status {
        stub_status on;
        access_log off;
        allow 127.0.0.1;
        deny all;
    }
}

# 导出关键指标：
# - Active connections（当前活跃连接数）
# - Requests/sec（请求速率）
# - 4xx/5xx 数量（限流/错误率）
```

### 5.3 关键监控指标

| 指标 | 告警阈值 | 说明 |
|---|---|---|
| `request_time` > 500ms 比例 | > 5% | 后端响应变慢 |
| `upstream_connect_time` > 1s | > 1% | 网关连接异常 |
| 429 响应比例 | > 30% | 限流触发过多，需扩缩容 |
| 5xx 比例 | > 1% | 后端异常 |
| Active connections | > 容量的 80% | 连接数不足 |

---

## 六、防刷安全配置

```nginx
# 防止直接 IP 访问绕过 CDN
server {
    listen 443 ssl;
    server_name _;
    return 444;  # 关闭连接，不返回任何响应
}

server {
    listen 80;
    server_name _;
    return 301 https://flash.maiya.youxuan.shop$request_uri;
}

# 防盗链（防止图片/静态资源被外部引用）
location /static/ {
    valid_referers server_names flash.maiya.youxuan.shop *.maiya.youxuan.shop;
    if ($invalid_referer) {
        return 403;
    }
}

# 限制请求方法
location /api/flash/grab {
    limit_except POST {
        deny all;
    }
}

# 限制请求体大小（秒杀请求体不超过 1KB）
client_max_body_size 1k;
```

---

## 七、完整配置汇总

```nginx
# /etc/nginx/nginx.conf — 核心配置

worker_processes auto;
worker_rlimit_nofile 65535;

events {
    use epoll;
    worker_connections 65535;
    accept_mutex on;
    multi_accept on;
}

http {
    include       mime.types;
    default_type  application/octet-stream;

    # 基础优化
    sendfile       on;
    tcp_nopush     on;
    tcp_nodelay    on;
    server_tokens  off;

    # 连接超时
    keepalive_timeout 30s;
    keepalive_requests 10000;

    # 客户端超时
    client_body_timeout   5s;
    client_header_timeout 5s;
    send_timeout          5s;

    # 缓冲区
    client_body_buffer_size 8k;
    client_header_buffer_size 4k;
    large_client_header_buffers 4 8k;

    #开放文件缓存
    open_file_cache max=65535 inactive=30s;
    open_file_cache_valid 60s;
    open_file_cache_min_uses 2;

    # Gzip
    gzip on;
    gzip_types application/json text/plain text/css;
    gzip_min_length 1000;
    gzip_proxied any;

    # 限流区域
    limit_req_zone $http_x_user_id zone=flash_user:10m rate=1000r/s;
    limit_req_zone $binary_remote_addr zone=flash_ip:10m rate=3r/s;
    limit_req_zone $request_uri zone=flash_grab:10m rate=5000r/s;
    limit_conn_zone $binary_remote_addr zone=perip:10m;

    # 共享内存（Lua 限流用）
    lua_shared_dict flash_limit 20m;

    # 上游
    upstream flash_backend {
        server 192.168.1.10:8080 weight=5;
        server 192.168.1.11:8080 weight=5;
        server 192.168.1.12:8080 weight=5;
        keepalive 128;
        keepalive_requests 10000;
        keepalive_timeout 30s;
    }

    # 日志格式
    log_format flash_log '$remote_addr - $http_x_user_id [$time_local] '
                         '"$request" $status $body_bytes_sent '
                         '$request_time $upstream_response_time '
                         '$upstream_addr "$http_x_forwarded_for"';

    include /etc/nginx/sites-enabled/*.conf;
}
```

```nginx
# /etc/nginx/sites-enabled/flash.conf

server {
    # SSL
    listen 443 ssl;
    server_name flash.maiya.youxuan.shop;

    ssl_certificate     /etc/nginx/certs/flash.pem;
    ssl_certificate_key /etc/nginx/certs/flash-key.pem;
    ssl_session_cache   shared:flash_ssl:50m;
    ssl_session_timeout 10m;
    ssl_protocols       TLSv1.2 TLSv1.3;

    # --- 静态资源 ---
    location /static/ {
        alias /data/flash-static/;
        expires 30d;
        add_header Cache-Control "public, immutable";
        access_log off;
    }

    # --- 秒杀抢购（严格限流） ---
    location /api/flash/grab {
        limit_req zone=flash_ip burst=5 nodelay;
        limit_req zone=flash_user burst=10;
        limit_req zone=flash_grab burst=1000 nodelay;
        limit_conn perip 5;
        limit_req_status 429;

        error_page 429 = @rate_limit_response;

        proxy_pass http://flash_backend;
        proxy_http_version 1.1;
        proxy_set_header Connection "";
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-User-Id $http_x_user_id;
        proxy_set_header Host $host;

        proxy_request_buffering off;
        proxy_buffering on;
        proxy_buffer_size 4k;
        proxy_buffers 8 4k;

        proxy_connect_timeout 2s;
        proxy_send_timeout    3s;
        proxy_read_timeout    5s;

        client_max_body_size 1k;
        limit_except POST { deny all; }

        access_log /var/log/nginx/flash-access.log flash_log buffer=32k flush=5s;
    }

    # --- 活动信息（温和限流） ---
    location /api/flash/ {
        limit_req zone=flash_ip burst=20 nodelay;
        proxy_pass http://flash_backend;
        proxy_http_version 1.1;
        proxy_set_header Connection "";
        proxy_buffering on;

        access_log /var/log/nginx/flash-access.log flash_log buffer=32k flush=5s;
    }

    # --- 限流响应 ---
    location @rate_limit_response {
        default_type application/json;
        return 429 '{"code":-6,"msg":"请求过于频繁，请稍后重试"}';
    }
}
```

---

## 八、部署检查清单

| 检查项 | 说明 | 验证方式 |
|---|---|---|
| **worker 数量** | `auto` 模式 = CPU 核数 | `nginx -T \| grep worker_processes` |
| **文件描述符** | `worker_rlimit_nofile 65535` | `ps aux \| grep nginx` 查看 limits |
| **epoll** | 事件模型用 epoll | `nginx -T \| grep epoll` |
| **SSL 会话复用** | 50MB 缓存 | `nginx -T \| grep ssl_session_cache` |
| **长连接** | keepalive 128 到后端 | `nginx -T \| grep keepalive` |
| **限流配置** | 三层限流已启用 | curl 压测验证 429 |
| **日志格式** | 含 request_time | 查看 access log 格式 |
| **健康检查** | upstream 健康检查 | 停掉一个网关看是否剔除 |

---

## 九、效果预估

以秒杀活动峰值 100 万 QPS 到达 CDN，CDN 收敛后 10 万 QPS 回源到 Nginx：

| 优化层 | 到达 QPS | 拦截比例 | 累计拦截 |
|---|---|---|---|
| CDN 收敛后回源 | 100,000 | — | — |
| Nginx IP 限流(3r/s/IP) | 100,000 → 15,000 | 85% | 85% |
| Nginx 用户限流(1r/s) | 15,000 → 5,000 | 67% | 95% |
| Nginx URI 限流(5000r/s) | 5,000 → 4,500 | 10% | 95.5% |
| **最终到网关** | **~4,500 QPS** | **—** | **总量 99.5% ↓** |
