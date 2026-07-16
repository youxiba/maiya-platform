package ny.shop.youxuan.common.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * IP 地址工具类
 *
 * 从 HTTP 请求中提取客户端真实 IP 地址。
 * 支持通过 X-Forwarded-For、Proxy-Client-IP 等代理头获取。
 */
public class IpUtils {

    private static final String UNKNOWN = "unknown";

    private IpUtils() {}

    /**
     * 获取客户端真实 IP 地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return "0.0.0.0";
        }

        String ip = request.getHeader("x-forwarded-for");
        if (isValidIp(ip)) return extractFirstIp(ip);

        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) return ip;

        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) return ip;

        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIp(ip)) return ip;

        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) return extractFirstIp(ip);

        ip = request.getRemoteAddr();
        return ip;
    }

    private static boolean isValidIp(String ip) {
        return !StringUtils.isBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip);
    }

    private static String extractFirstIp(String ip) {
        if (ip != null && ip.contains(",")) {
            return ip.split(",")[0].trim();
        }
        return ip;
    }
}
