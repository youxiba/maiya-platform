package ny.shop.youxuan.paymentservice.pay.wxpay;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

/**
 * 微信支付客户端
 *
 * 协议：XML over HTTPS
 * 签名：MD5 (key=商户密钥)
 * 金额单位：分
 *
 * API 文档：https://pay.weixin.qq.com/wiki/doc/api/app/app.php
 */
@Component
public class WxPayClient {

    private static final Logger log = LoggerFactory.getLogger(WxPayClient.class);

    private static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    private static final String QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    @Value("${wxpay.app-id}")
    private String appId;

    @Value("${wxpay.mch-id}")
    private String mchId;

    @Value("${wxpay.mch-key}")
    private String mchKey;

    @Value("${wxpay.notify-url}")
    private String notifyUrl;

    @Value("${wxpay.cert-path:}")
    private String certPath;

    // ==================== APP 支付 ====================

    /**
     * APP 统一下单
     *
     * @param orderId 商户订单号
     * @param totalFee 金额（分）
     * @param ip 用户IP
     * @return 包含 prepay_id 等参数，客户端用此调起微信
     */
    public JSONObject createAppOrder(String orderId, String totalFee, String ip) {
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", appId);
        params.put("mch_id", mchId);
        params.put("nonce_str", generateNonce());
        params.put("body", "麦芽优选");
        params.put("out_trade_no", orderId);
        params.put("total_fee", totalFee);
        params.put("spbill_create_ip", ip);
        params.put("notify_url", notifyUrl);
        params.put("trade_type", "APP");

        String xml = postXml(UNIFIED_ORDER_URL, params);
        Map<String, String> result = parseXml(xml);

        if (!"SUCCESS".equals(result.get("return_code"))) {
            log.error("微信统一下单失败: orderId={}, return_msg={}", orderId, result.get("return_msg"));
            throw new RuntimeException("微信下单失败: " + result.get("return_msg"));
        }
        if (!"SUCCESS".equals(result.get("result_code"))) {
            log.error("微信统一下单业务失败: orderId={}, err_code={}, err_code_des={}",
                orderId, result.get("err_code"), result.get("err_code_des"));
            throw new RuntimeException("微信下单失败: " + result.get("err_code_des"));
        }

        String prepayId = result.get("prepay_id");

        // 生成客户端调起支付所需的参数
        JSONObject payParams = new JSONObject();
        payParams.put("appid", appId);
        payParams.put("partnerid", mchId);
        payParams.put("prepayid", prepayId);
        payParams.put("package", "Sign=WXPay");
        payParams.put("noncestr", generateNonce());
        payParams.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

        // 对客户端参数签名
        Map<String, String> signParams = new TreeMap<>();
        signParams.put("appid", appId);
        signParams.put("partnerid", mchId);
        signParams.put("prepayid", prepayId);
        signParams.put("package", "Sign=WXPay");
        signParams.put("noncestr", payParams.getString("noncestr"));
        signParams.put("timestamp", payParams.getString("timestamp"));
        payParams.put("sign", generateSign(signParams));

        log.info("微信统一下单成功: orderId={}, prepayId={}", orderId, prepayId);
        return payParams;
    }

    /**
     * 小程序统一下单
     */
    public JSONObject createXcxOrder(String orderId, String totalFee, String ip, String openId) {
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", appId);
        params.put("mch_id", mchId);
        params.put("nonce_str", generateNonce());
        params.put("body", "麦芽优选");
        params.put("out_trade_no", orderId);
        params.put("total_fee", totalFee);
        params.put("spbill_create_ip", ip);
        params.put("notify_url", notifyUrl);
        params.put("trade_type", "JSAPI");
        params.put("openid", openId);

        String xml = postXml(UNIFIED_ORDER_URL, params);
        Map<String, String> result = parseXml(xml);

        if (!"SUCCESS".equals(result.get("return_code"))) {
            throw new RuntimeException("小程序下单失败: " + result.get("return_msg"));
        }

        String prepayId = result.get("prepay_id");

        JSONObject payParams = new JSONObject();
        payParams.put("appId", appId);
        payParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        payParams.put("nonceStr", generateNonce());
        payParams.put("package", "prepay_id=" + prepayId);
        payParams.put("signType", "MD5");

        Map<String, String> signMap = new TreeMap<>();
        signMap.put("appId", appId);
        signMap.put("timeStamp", payParams.getString("timeStamp"));
        signMap.put("nonceStr", payParams.getString("nonceStr"));
        signMap.put("package", payParams.getString("package"));
        signMap.put("signType", "MD5");
        payParams.put("paySign", generateSign(signMap));

        log.info("小程序统一下单成功: orderId={}", orderId);
        return payParams;
    }

    // ==================== 回调验签 ====================

    /**
     * 验证微信回调签名
     *
     * @param params 回调XML解析后的参数Map
     * @return true=验签通过
     */
    public boolean verifyNotify(Map<String, String> params) {
        String sign = params.get("sign");
        if (sign == null) return false;

        // 计算签名时不包含 sign 本身
        Map<String, String> toSign = new TreeMap<>(params);
        toSign.remove("sign");

        String calculatedSign = generateSign(toSign);
        boolean result = sign.equals(calculatedSign);
        if (!result) {
            log.warn("微信回调验签失败: expected={}, actual={}", calculatedSign, sign);
        }
        return result;
    }

    // ==================== 退款 ====================

    /**
     * 微信退款（需要加载证书）
     *
     * @param orderId     商户订单号
     * @param transactionId 微信交易号
     * @param totalFee    订单总金额（分）
     * @param refundFee   退款金额（分）
     * @return true=退款成功
     */
    public boolean refund(String orderId, String transactionId, String totalFee, String refundFee) {
        if (certPath == null || certPath.isEmpty()) {
            log.error("微信退款证书未配置: certPath={}", certPath);
            return false;
        }

        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", appId);
        params.put("mch_id", mchId);
        params.put("nonce_str", generateNonce());
        params.put("out_trade_no", orderId);
        params.put("transaction_id", transactionId);
        params.put("out_refund_no", orderId + "_" + System.currentTimeMillis());
        params.put("total_fee", totalFee);
        params.put("refund_fee", refundFee);
        params.put("refund_desc", "订单退款");

        String xml = postXmlWithCert(REFUND_URL, params);

        Map<String, String> result = parseXml(xml);
        if ("SUCCESS".equals(result.get("return_code")) && "SUCCESS".equals(result.get("result_code"))) {
            log.info("微信退款成功: orderId={}, refundFee={}", orderId, refundFee);
            return true;
        }

        log.error("微信退款失败: orderId={}, err_code={}, err_code_des={}",
            orderId, result.get("err_code"), result.get("err_code_des"));
        return false;
    }

    // ==================== 内部工具方法 ====================

    /** 生成随机字符串 */
    private String generateNonce() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    /** MD5 签名 */
    private String generateSign(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            if (e.getValue() != null && !e.getValue().isEmpty()) {
                sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
            }
        }
        sb.append("key=").append(mchKey);
        return md5(sb.toString()).toUpperCase();
    }

    private String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5计算失败", e);
        }
    }

    /** Map 转 XML */
    private String mapToXml(Map<String, String> params) {
        StringBuilder xml = new StringBuilder("<xml>");
        for (Map.Entry<String, String> e : params.entrySet()) {
            xml.append("<").append(e.getKey()).append(">")
               .append("<![CDATA[").append(e.getValue()).append("]]>")
               .append("</").append(e.getKey()).append(">");
        }
        xml.append("</xml>");
        return xml.toString();
    }

    /** XML 解析为 Map */
    private Map<String, String> parseXml(String xml) {
        Map<String, String> map = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-type", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            Element root = doc.getDocumentElement();
            NodeList list = root.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                if (list.item(i) instanceof Element) {
                    Element el = (Element) list.item(i);
                    map.put(el.getTagName(), el.getTextContent());
                }
            }
        } catch (Exception e) {
            log.error("XML解析失败: {}", xml, e);
        }
        return map;
    }

    /** POST XML（无证书） */
    private String postXml(String urlStr, Map<String, String> params) {
        // 加入签名
        SortedMap<String, String> signed = new TreeMap<>(params);
        String sign = generateSign(signed);
        signed.put("sign", sign);
        String xml = mapToXml(signed);

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "text/xml");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(xml.getBytes(StandardCharsets.UTF_8));
            }

            try (InputStream is = conn.getInputStream()) {
                byte[] buf = new byte[8192];
                int len = is.read(buf);
                return new String(buf, 0, len, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("微信HTTP请求失败", e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[" + e.getMessage() + "]]></return_msg></xml>";
        }
    }

    /** POST XML（双向证书 - 退款用） */
    private String postXmlWithCert(String urlStr, Map<String, String> params) {
        // 加入签名
        SortedMap<String, String> signed = new TreeMap<>(params);
        signed.put("sign", generateSign(signed));
        String xml = mapToXml(signed);

        // TODO: 使用证书的 HTTPS 请求（需加载 p12 证书）
        // 简化：调用普通 POST（生产环境需使用 SSLContext + KeyStore）
        log.warn("退款请求使用简化HTTPS（生产环境需加载证书）: {}", urlStr);
        return postXml(urlStr, params);
    }
}
