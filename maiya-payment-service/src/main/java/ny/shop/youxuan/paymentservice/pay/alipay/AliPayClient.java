package ny.shop.youxuan.paymentservice.pay.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付宝支付客户端
 *
 * 集成 alipay-sdk-java 4.40.890.ALL
 * 使用 公钥证书模式 (RSA2)
 */
@Component
public class AliPayClient {

    private static final Logger log = LoggerFactory.getLogger(AliPayClient.class);

    @Value("${alipay.app-id}")
    private String appId;

    @Value("${alipay.private-key}")
    private String privateKey;

    @Value("${alipay.alipay-public-key}")
    private String alipayPublicKey;

    @Value("${alipay.notify-url}")
    private String notifyUrl;

    @Value("${alipay.gateway-url:https://openapi.alipay.com/gateway.do}")
    private String gatewayUrl;

    private AlipayClient alipayClient;

    @PostConstruct
    public void init() {
        // 实例化支付宝客户端（公钥模式，非证书模式）
        // 生产环境使用证书模式: CertificateAlipayRequest
        alipayClient = new DefaultAlipayClient(
            gatewayUrl,
            appId,
            privateKey,
            "json",
            "UTF-8",
            alipayPublicKey,
            "RSA2"
        );
        log.info("支付宝客户端初始化完成, appId={}", appId);
    }

    /**
     * APP 支付 - 生成 order string（客户端用此调起支付宝）
     *
     * @param orderId   商户订单号
     * @param totalFee  订单总金额（元，两位小数）
     * @param subject   订单标题
     * @return order string，客户端SDK用此参数调起支付宝
     */
    public String createAppPayOrder(String orderId, String totalFee, String subject) {
        // 业务参数
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("total_amount", totalFee);
        bizContent.put("subject", subject);
        bizContent.put("product_code", "QUICK_MSECURITY_PAY"); // APP支付

        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setBizContent(bizContent.toJSONString());
        request.setNotifyUrl(notifyUrl);

        try {
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            if (response.isSuccess()) {
                log.info("支付宝统一下单成功: orderId={}, totalFee={}", orderId, totalFee);
                return response.getBody(); // 返回 order string
            } else {
                log.error("支付宝统一下单失败: orderId={}, code={}, msg={}",
                    orderId, response.getCode(), response.getMsg());
                throw new RuntimeException("支付宝下单失败: " + response.getSubMsg());
            }
        } catch (AlipayApiException e) {
            log.error("支付宝统一下单异常: orderId=" + orderId, e);
            throw new RuntimeException("支付宝下单异常", e);
        }
    }

    /**
     * 验证支付宝异步通知签名
     *
     * @param params 支付宝回调的所有参数
     * @return true=验签通过
     */
    public boolean verifyNotify(Map<String, String> params) {
        try {
            return AlipaySignature.rsaCheckV1(params, alipayPublicKey, "UTF-8", "RSA2");
        } catch (AlipayApiException e) {
            log.error("支付宝回调验签失败", e);
            return false;
        }
    }

    /**
     * 退款
     *
     * @param orderId       商户订单号
     * @param tradeNo       支付宝交易号
     * @param refundAmount  退款金额（元）
     * @param refundReason  退款原因
     * @return true=退款成功
     */
    public boolean refund(String orderId, String tradeNo, String refundAmount, String refundReason) {
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("trade_no", tradeNo);
        bizContent.put("refund_amount", refundAmount);
        if (refundReason != null) {
            bizContent.put("refund_reason", refundReason);
        }

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent(bizContent.toJSONString());

        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("支付宝退款成功: orderId={}, amount={}", orderId, refundAmount);
                return true;
            } else {
                log.error("支付宝退款失败: orderId={}, code={}, msg={}",
                    orderId, response.getCode(), response.getSubMsg());
                return false;
            }
        } catch (AlipayApiException e) {
            log.error("支付宝退款异常: orderId=" + orderId, e);
            return false;
        }
    }
}
