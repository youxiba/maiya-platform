# 秒杀系统 CDN 层优化方案

> 基于 maiya-platform 秒杀系统架构的 CDN 层优化方案

## 架构总览

```
用户 → DNS → CDN边缘节点 → 源站(网关) → 秒杀服务 → Redis Lua → MQ

                        CDN 边缘层
  ┌──────────────────────────────────────────────┐
  │  ① 静态缓存（活动页/商品图/JS/CSS）            │
  │  ② Edge Function：请求收敛 + 预校验 + 限流     │
  │  ③ 动态缓存（活动元信息 TTL 1~5s）             │
  └──────┬──────────────┬───────────────────────┘
         │ (hit)         │ (miss / 写请求)
         │               ▼
         │          源站 → 网关 → 秒杀服务 → Redis → MQ
         │
   返回 CDN 缓存
```

---

## 一、静态资源缓存（基础层）

秒杀活动页面的可缓存静态资源提前预热到 CDN 所有边缘节点。

| 资源 | 缓存策略 | 说明 |
|---|---|---|
| 活动 HTML 页面 | 提前预热 + TTL 长期 | 生成静态页，CDN 全节点分发 |
| 商品图片（缩略图） | 永久缓存 | 带有版本号/指纹，上线即预热 |
| CSS/JS 包 | 永久缓存 | Webpack 打包带 hash |
| 秒杀倒计时组件 JS | 活动期间固定 | 不依赖用户状态的部分 |

**预热脚本（在活动上线前执行）：**

```bash
# 以阿里云 CDN 为例
aliyun cdn PreloadObjectCaches \
  --ObjectPath "https://maiya.youxuan.shop/flash/20260720/index.html"
```

---

## 二、Edge Function 请求收敛（核心优化层）

在边缘节点用计算能力拦截无效请求，不让他们到达源站。

```
用户请求 → CDN Edge Function
              │
     ┌────────┼──────────┐
     ▼        ▼          ▼
  ① 预校验  ② 限流     ③ 合并
```

### 完整 Edge Function 实现

以下为生产级 Edge Function 实现，将预校验、限流、请求合并三个功能整合在一个入口：

```javascript
// ============================================================
// flash-edge-handler.js
// 部署位置：CDN Edge Function（阿里云 EdgeRoutine / 腾讯云 EdgeOne / Cloudflare Workers）
// 功能：秒杀抢购请求的边缘收敛 — 预校验 + 限流 + 合并
// ============================================================

// -------- 配置 ----------
const CONFIG = {
  // 限流阈值
  rateLimit: {
    perIp: 3,          // 每个 IP 每秒最多 3 次
    perUser: 1,        // 每个用户每秒最多 1 次
    perSku: 5000,      // 每个 SKU 每秒最多 5000 次
    windowMs: 1000,    // 统计窗口 1 秒
  },
  // 请求合并窗口（毫秒）
  dedupWindowMs: 50,
  // 需要保护的 API 路径
  protectedPaths: ['/api/flash/grab'],
  // 边缘 KV 前缀
  kvPrefix: {
    flashInfo: 'flash:info:',
    rateLimit:  'flash:rl:',
    dedup:      'flash:dd:',
  },
  // 签名密钥（从环境变量读取）
  signSecret: EDGE_ENV.get('FLASH_SIGN_SECRET') || '',
};

// -------- 工具函数 ----------

/** 安全解析 JSON，失败返回 null */
function safeParse(json) {
  try { return JSON.parse(json); } catch { return null; }
}

/** 提取客户端 IP（兼容各种 CDN 头部） */
function getClientIP(request) {
  return request.headers.get('X-Forwarded-For')?.split(',')[0]?.trim()
      || request.headers.get('X-Real-IP')
      || request.headers.get('CF-Connecting-IP')
      || '0.0.0.0';
}

/** 构建统一响应 */
function jsonResponse(data, status = 200) {
  return new Response(JSON.stringify(data), {
    status,
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
      'X-CDN-Cache': 'MISS',
      'Access-Control-Allow-Origin': '*',
    },
  });
}

/** 构建限流/合并后的直接返回（hit 响应） */
function cdnDirectResponse(data) {
  return new Response(JSON.stringify(data), {
    status: 200,
    headers: {
      'Content-Type': 'application/json;charset=utf-8',
      'X-CDN-Cache': 'HIT',
      'X-CDN-Direct': '1',
      'Access-Control-Allow-Origin': '*',
    },
  });
}

// -------- ① 活动预校验 ----------

/**
 * 检查活动有效性
 * 从边缘 KV 读取活动元信息（由源站定时推送）
 */
async function validateFlashActivity(fsId) {
  const key = CONFIG.kvPrefix.flashInfo + fsId;
  const raw = await EDGE_KV.get(key);
  if (!raw) return { valid: false, code: -3, msg: '活动不存在' };

  const info = safeParse(raw);
  if (!info) return { valid: false, code: -99, msg: '活动数据异常' };

  const now = Date.now();
  if (now < info.startTime) {
    return { valid: false, code: -3, msg: '活动尚未开始' };
  }
  if (now > info.endTime) {
    return { valid: false, code: -3, msg: '活动已结束' };
  }
  if (info.status !== 'ACTIVE') {
    return { valid: false, code: -3, msg: '活动已关闭' };
  }

  return { valid: true, info };
}

/**
 * 签名校验（防止伪造请求直接打到源站）
 * 客户端请求头带 X-Sign: HMAC-SHA256(method+path+timestamp+body, secret)
 */
function verifySignature(request, body) {
  const sign = request.headers.get('X-Sign');
  const timestamp = request.headers.get('X-Timestamp');
  if (!sign || !timestamp) return false;

  // 5 分钟过期，防止重放
  if (Math.abs(Date.now() - Number(timestamp)) > 300_000) return false;

  const method = request.method;
  const path = new URL(request.url).pathname;
  const payload = `${method}${path}${timestamp}${body || ''}`;

  const expected = crypto.hmac.sha256(CONFIG.signSecret, payload);
  return crypto.timingSafeEqual(sign, expected);
}

// -------- ② 边缘限流 ----------

/**
 * 滑动窗口限流（基于边缘 KV）
 * key 结构：flash:rl:{维度}:{维度值}:{窗口序号}
 */
async function checkRateLimit(dimension, value) {
  if (!value) return { allowed: true };

  const windowSeq = Math.floor(Date.now() / CONFIG.rateLimit.windowMs);
  const key = `${CONFIG.kvPrefix.rateLimit}${dimension}:${value}:${windowSeq}`;

  // 原子递增当前窗口计数
  const count = await EDGE_KV.increment(key, 1, { ttl: 2 }); // TTL 2 秒兜底

  let limit;
  switch (dimension) {
    case 'ip':   limit = CONFIG.rateLimit.perIp;   break;
    case 'user': limit = CONFIG.rateLimit.perUser; break;
    case 'sku':  limit = CONFIG.rateLimit.perSku;  break;
    default:     limit = Infinity;
  }

  if (count > limit) {
    return { allowed: false, retryAfterMs: CONFIG.rateLimit.windowMs };
  }
  return { allowed: true };
}

// -------- ③ 请求合并 ----------

/**
 * 同一用户在同一秒杀活动上，dedupWindowMs 内只放行第一个请求
 * 后续请求直接返回第一个请求的响应（缓存在 KV 中）
 */
async function dedupRequest(fsId, uid) {
  const dedupKey = `${CONFIG.kvPrefix.dedup}${uid}:${fsId}`;

  // 检查是否有正在处理中的请求
  const inflight = await EDGE_KV.get(dedupKey);
  if (inflight) {
    // 50ms 内的重复请求 — 返回排队中提示，避免穿透
    return { deduped: true };
  }

  // 标记该用户正在处理中
  await EDGE_KV.put(dedupKey, String(Date.now()), {
    ttl: Math.ceil(CONFIG.dedupWindowMs / 1000) + 1,
  });

  return { deduped: false };
}

// -------- 主入口 ----------

async function handleRequest(request) {
  const url = new URL(request.url);
  const path = url.pathname;
  const method = request.method;

  // ----- 只拦截秒杀抢购 POST 请求 -----
  if (method === 'POST' && CONFIG.protectedPaths.includes(path)) {
    const startTime = Date.now();
    let rejectReason = null;

    try {
      // 解析请求体
      const bodyText = await request.text();
      const body = safeParse(bodyText) || {};
      const fsId = body.fsId || url.searchParams.get('fsId');
      const uid   = body.uid   || url.searchParams.get('uid')
                   || request.headers.get('X-User-Id');
      const clientIP = getClientIP(request);

      // 必填参数校验
      if (!fsId || !uid) {
        return jsonResponse({ code: -4, msg: '参数不完整' });
      }

      // ----- ① 活动预校验 -----
      const validation = await validateFlashActivity(fsId);
      if (!validation.valid) {
        rejectReason = 'precheck';
        return jsonResponse({ code: validation.code, msg: validation.msg });
      }

      // ----- ② 请求合并（50ms 窗口） -----
      const dedup = await dedupRequest(fsId, uid);
      if (dedup.deduped) {
        rejectReason = 'dedup';
        return cdnDirectResponse({
          code: 0,
          msg: '请求已提交，请勿重复点击',
        });
      }

      // ----- ③ 三层限流 -----
      // IP 限流
      const ipLimit = await checkRateLimit('ip', clientIP);
      if (!ipLimit.allowed) {
        rejectReason = 'rate_limit_ip';
        return cdnDirectResponse({
          code: -6, msg: '请求过于频繁，请稍后再试',
        });
      }

      // 用户限流
      const userLimit = await checkRateLimit('user', uid);
      if (!userLimit.allowed) {
        rejectReason = 'rate_limit_user';
        return cdnDirectResponse({
          code: -6, msg: '操作太快了，请稍后重试',
        });
      }

      // SKU 级限流（全局）
      const skuLimit = await checkRateLimit('sku', fsId);
      if (!skuLimit.allowed) {
        rejectReason = 'rate_limit_sku';
        return cdnDirectResponse({
          code: -7, msg: '当前抢购人数过多，请重试',
        });
      }

      // ----- 所有校验通过，放行到源站 -----
      // 组装请求体（去除不需要的字段）
      const upstreamBody = JSON.stringify({ fsId, uid });

      const upstreamResponse = await fetch(new Request(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-Real-IP': clientIP,
          'X-User-Id': uid,
          'X-Edge-Timestamp': String(startTime),
        },
        body: upstreamBody,
        redirect: 'manual',
      }));

      // 读取源站响应
      const upstreamData = await upstreamResponse.text();
      const result = safeParse(upstreamData) || { code: -99, msg: '源站异常' };

      // 清除合并标记（请求已处理完成）
      const dedupKey = `${CONFIG.kvPrefix.dedup}${uid}:${fsId}`;
      await EDGE_KV.delete(dedupKey);

      return new Response(JSON.stringify(result), {
        status: upstreamResponse.status,
        headers: {
          'Content-Type': 'application/json;charset=utf-8',
          'X-CDN-Upstream-Time': String(Date.now() - startTime),
        },
      });

    } catch (err) {
      // 边缘异常 — 降级放行到源站，保障业务可用
      console.error(`EdgeFunction error: ${err.message}`);

      // 尝试直接转发原请求
      try {
        return await fetch(request);
      } catch {
        return jsonResponse({ code: -99, msg: '系统繁忙，请稍后重试' }, 502);
      }
    }
  }

  // ----- 非秒杀请求，直接放行 -----
  return fetch(request);
}

// Edge Function 入口绑定
addEventListener('fetch', event => {
  event.respondWith(handleRequest(event.request));
});
```

### 限流配置对照

| 维度 | 阈值 | 边缘 KV Key 示例 | 防什么 |
|---|---|---|---|
| 每个 IP | 3 次/秒 | `flash:rl:ip:192.168.1.1:86400` | 脚本刷单 |
| 每个用户 | 1 次/秒 | `flash:rl:user:u10086:86400` | 正常用户防手抖 |
| 每个 SKU | 5000 次/秒 | `flash:rl:sku:FS20260720:86400` | 整体防 DDoS |

### 请求合并流程

```
用户A 第1次请求 (T0)     → 放入行 → KV 写入标记 → 回源 → 成功 → 清除标记
用户A 第2次请求 (T0+10ms) → 命中合并 → 直接返回"已提交，请勿重复点击"
用户A 第3次请求 (T0+60ms) → 合并窗口已过 → 正常放行
```

### 数据同步：源站 → 边缘 KV

Edge Function 依赖边缘 KV 中的活动元信息，由源站定时推送：

```java
// FlashEdgeKvSyncTask.java — 源站定时任务
@Component
public class FlashEdgeKvSyncTask {

    @Autowired
    private FlashSaleMapper flashSaleMapper;

    // 每 5 秒同步一次有效活动到 CDN 边缘 KV
    @Scheduled(fixedRate = 5000)
    public void syncActiveFlashToEdgeKv() {
        List<FlashSale> activeSales = flashSaleMapper.findActiveNow();

        for (FlashSale sale : activeSales) {
            JSONObject info = new JSONObject();
            info.put("fsId", sale.getFsId());
            info.put("status", "ACTIVE");
            info.put("startTime", sale.getStartTime().getTime());
            info.put("endTime", sale.getEndTime().getTime());
            info.put("limitQty", sale.getLimitQty());
            info.put("whitelist", sale.getWhitelist());

            // 调用 CDN 厂商 API 写入边缘 KV
            edgeKvClient.put("flash:info:" + sale.getFsId(), info.toJSONString(), 30);
            //                                          TTL=30秒，与同步周期匹配
        }
    }
}
```

### 部署配置示例（阿里云 EdgeRoutine）

```javascript
// 阿里云 DCDN EdgeRoutine 控制台配置
// 1. 创建 ER 模板，粘贴 flash-edge-handler.js
// 2. 配置路由：/* → 该 ER 模板
// 3. 创建边缘 KV（flash-cache），绑定到 ER
// 4. 环境变量：
//    FLASH_SIGN_SECRET = 从密钥管理服务读取
//
// 关联的 CDN 域名：flash.maiya.youxuan.shop
// 回源地址：maiya-gateway:8080
```

```yaml
# 腾讯云 EdgeOne 配置片段
RulesEngine:
  - Rule: 秒杀抢购请求收敛
    Match: request.url.path == "/api/flash/grab"
    Actions:
      - EdgeFunction: flash-edge-handler
  - Rule: 活动信息动态缓存
    Match: request.url.path == "/api/flash/info"
    Actions:
      - CacheTTL: 5s
      - CacheKey: "flash_info_{query.fsId}"
```

---

## 三、动态缓存（减轻 Redis 压力）

活动元信息（活动名称、时间、限购数量等不常变化的数据）在 CDN 边缘短时间缓存：

| 接口 | 缓存 TTL | 缓存键 |
|---|---|---|
| `GET /api/flash/info` | 5 秒 | `flash:info:{fsId}` |
| `GET /api/flash/list` | 3 秒 | `flash:list` |
| `GET /api/flash/stock` | 1 秒 | 不缓存，实时回源 |

CDN 配置示例（阿里云 CDN）：

```nginx
# CDN 缓存规则
location /api/flash/info {
    set_cache_ttl 5s;
    set_cache_key "flash_info_$arg_fsId";
}

location /api/flash/list {
    set_cache_ttl 3s;
    set_cache_key "flash_list";
}

location /api/flash/grab {
    # 写请求不缓存
    set_cache_ttl 0;
}
```

---

## 四、流量效果预估

以一个秒杀活动（100 万并发请求）为例：

| 阶段 | 请求量 | 到源站 | 源站节省 |
|---|---|---|---|
| **无 CDN** | 1,000,000 | 1,000,000 | — |
| **+静态缓存** | 1,000,000 | 800,000 | 20% ↓ |
| **+边缘预校验** | 800,000 | 120,000 | 85% ↓ |
| **+边缘限流** | 120,000 | 30,000 | 75% ↓ |
| **+请求合并** | 30,000 | 8,000 | 73% ↓ |
| **最终到源站** | — | **~8,000** | **总量 99.2% ↓** |

---

## 五、实施步骤建议

| 步骤 | 内容 | 优先级 |
|---|---|---|
| **1** | 静态资源（图片/CSS/JS）接入 CDN + 预热 | P0 |
| **2** | 活动页全站静态化，CDN 分发 | P0 |
| **3** | 活动元信息接口动态缓存（TTL 3~5s） | P1 |
| **4** | Edge Function 做预校验（活动状态检查） | P1 |
| **5** | 边缘限流（IP + 用户级别） | P2 |
| **6** | 边缘请求合并 | P2 |

---

## 六、注意事项

- **库存一致性**：CDN 缓存的是活动元信息，库存实时查询必须回源，不能用 CDN 缓存库存数据
- **Edge KV 同步延迟**：活动状态变更（开始/结束）推送到 CDN 边缘 KV 时可能有秒级延迟，需要提前几秒切换状态作为 buffer
- **回源带宽**：即使只放行 1% 的请求，也要提前压测网关和 Redis 的承载能力
- **CDN 成本**：Edge Function 调用量 ≈ 请求总量，成本高于普通 CDN 回源，需要评估
