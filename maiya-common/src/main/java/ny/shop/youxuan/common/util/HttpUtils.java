package ny.shop.youxuan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP 请求工具类
 *
 * 提供基于 HttpURLConnection 的 GET/POST 请求方法。
 * 适用于轻量级 HTTP 调用（如微信支付、第三方 API 回调）。
 */
public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 15000;

    private HttpUtils() {}

    /**
     * GET 请求
     */
    public static String get(String urlString) {
        return request(urlString, null, null, "GET");
    }

    /**
     * GET 请求（带自定义 Header）
     */
    public static String get(String urlString, Map<String, String> headers) {
        return request(urlString, null, headers, "GET");
    }

    /**
     * POST 请求（JSON body）
     */
    public static String postJson(String urlString, String jsonBody) {
        return request(urlString, jsonBody, Map.of("Content-Type", "application/json;charset=utf-8"), "POST");
    }

    /**
     * POST 请求（XML body）
     */
    public static String postXml(String urlString, String xmlBody) {
        return request(urlString, xmlBody, Map.of("Content-Type", "text/xml;charset=utf-8"), "POST");
    }

    /**
     * POST 请求（表单）
     */
    public static String postForm(String urlString, String formBody) {
        return request(urlString, formBody, Map.of("Content-Type", "application/x-www-form-urlencoded;charset=utf-8"), "POST");
    }

    /**
     * 通用 HTTP 请求
     */
    public static String request(String urlString, String body, Map<String, String> headers, String method) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestMethod(method.toUpperCase());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Charset", "UTF-8");

            // 设置自定义 Header
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // 写入请求体
            if (body != null && !body.isEmpty()) {
                try (OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)) {
                    osw.write(body);
                    osw.flush();
                }
            }

            // 读取响应
            int responseCode = conn.getResponseCode();
            InputStream inputStream = (responseCode >= 200 && responseCode < 300)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            if (inputStream == null) {
                return responseCode == 204 ? "success" : "error";
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }

        } catch (Exception e) {
            log.error("HTTP请求异常: url={}, method={}", urlString, method, e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
