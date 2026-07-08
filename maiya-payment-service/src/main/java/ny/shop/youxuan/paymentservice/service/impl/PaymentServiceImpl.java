package ny.shop.youxuan.paymentservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ny.shop.youxuan.common.event.PaymentSuccessEvent;
import ny.shop.youxuan.paymentservice.entity.PaymentRecord;
import ny.shop.youxuan.paymentservice.mapper.PaymentRecordMapper;
import ny.shop.youxuan.paymentservice.mq.PaymentEventProducer;
import ny.shop.youxuan.paymentservice.pay.alipay.AliPayClient;
import ny.shop.youxuan.paymentservice.pay.wxpay.WxPayClient;
import ny.shop.youxuan.paymentservice.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired private PaymentRecordMapper paymentRecordMapper;
    @Autowired private PaymentEventProducer eventProducer;
    @Autowired private AliPayClient aliPayClient;
    @Autowired private WxPayClient wxPayClient;

    @Override
    public JSONObject unifiedPay(String orderId, String payType, Integer fee, String uid, String ip) {
        String feeYuan = BigDecimal.valueOf(fee).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN).toString();
        JSONObject result = new JSONObject();

        switch (payType) {
            case "ALI_PAY": {
                // 调用支付宝 SDK 统一下单，返回 order string
                String orderStr = aliPayClient.createAppPayOrder(orderId, feeYuan, "麦芽优选");
                result.put("payData", orderStr);
                result.put("payType", "ALI_PAY");
                break;
            }
            case "WX_PAY": {
                JSONObject wxResult = wxPayClient.createAppOrder(orderId, String.valueOf(fee), ip);
                result.put("payData", wxResult);
                result.put("payType", "WX_PAY");
                break;
            }
            case "WX_XCX_PAY": {
                // openId 需要客户端传入，此处简化处理
                JSONObject xcxResult = wxPayClient.createXcxOrder(orderId, String.valueOf(fee), ip, "");
                result.put("payData", xcxResult);
                result.put("payType", "WX_XCX_PAY");
                break;
            }
            default:
                throw new IllegalArgumentException("不支持的支付类型: " + payType);
        }

        // 记录支付流水
        PaymentRecord record = new PaymentRecord();
        record.setOrderId(orderId);
        record.setPayType(payType);
        record.setFee(BigDecimal.valueOf(fee));
        record.setTradeStatus("PENDING");
        record.setCreateTime(System.currentTimeMillis());
        paymentRecordMapper.insert(record);

        log.info("统一下单: orderId={}, payType={}, fee={}", orderId, payType, feeYuan);
        return result;
    }

    /** 支付宝异步回调处理 */
    @Override
    public String handleAliNotify(HttpServletRequest req, HttpServletResponse resp) {
        // 1. 提取回调参数
        Map<String, String> params = new HashMap<>();
        Enumeration<String> names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            params.put(name, req.getParameter(name));
        }

        // 2. 验签 - 防止伪造回调
        boolean signVerified = aliPayClient.verifyNotify(params);
        if (!signVerified) {
            log.error("支付宝回调验签失败");
            return "failure";
        }

        String tradeStatus = params.get("trade_status");
        String orderId = params.get("out_trade_no");
        String tradeNo = params.get("trade_no");

        if (!"TRADE_SUCCESS".equals(tradeStatus)) {
            log.warn("支付宝回调非成功状态: {}", tradeStatus);
            return "failure";
        }

        // 3. 处理支付成功（幂等 + MQ 事件）
        processPaymentCallback(orderId, tradeNo, "ALI_PAY", params.get("total_amount"));
        return "success";
    }

    /** 微信支付回调处理 */
    @Override
    public String handleWxNotify(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // 1. 读取回调 XML
            StringBuilder xmlSb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) xmlSb.append(line);
            }

            // 2. 解析 XML
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(xmlSb.toString().getBytes("UTF-8"));
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(bis);
            org.w3c.dom.Element root = doc.getDocumentElement();

            java.util.Map<String, String> params = new java.util.HashMap<>();
            org.w3c.dom.NodeList list = root.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                if (list.item(i) instanceof org.w3c.dom.Element) {
                    org.w3c.dom.Element el = (org.w3c.dom.Element) list.item(i);
                    params.put(el.getTagName(), el.getTextContent());
                }
            }

            // 3. 验签
            if (!wxPayClient.verifyNotify(params)) {
                log.error("微信回调验签失败");
                return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg>验签失败</return_msg></xml>";
            }

            String resultCode = params.get("result_code");
            String orderId = params.get("out_trade_no");
            String tradeNo = params.get("transaction_id");

            if ("SUCCESS".equals(resultCode) && orderId != null && tradeNo != null) {
                processPaymentCallback(orderId, tradeNo, "WX_PAY", params.get("total_fee"));
            }

            return "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
        } catch (Exception e) {
            log.error("微信回调处理失败", e);
            return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg>系统异常</return_msg></xml>";
        }
    }

    @Override
    public String handleWxXcxNotify(HttpServletRequest req, HttpServletResponse resp) {
        return handleWxNotify(req, resp);
    }

    /**
     * 核心：支付回调处理
     * 幂等校验 → 更新记录 → 发 MQ 事件
     */
    @Transactional
    public void processPaymentCallback(String orderId, String transactionId, String payType, String amountStr) {
        // 幂等校验
        PaymentRecord existing = paymentRecordMapper.findByTransactionId(transactionId);
        if (existing != null) {
            log.info("回调已处理，跳过幂等: transactionId={}", transactionId);
            return;
        }

        PaymentRecord record = paymentRecordMapper.findByOrderId(orderId);
        if (record != null) {
            record.setTransactionId(transactionId);
            record.setTradeStatus("SUCCESS");
            record.setNotifyTime(System.currentTimeMillis());
            paymentRecordMapper.updateById(record);
        }

        // 发 MQ 事件 → Order Service 消费
        PaymentSuccessEvent event = new PaymentSuccessEvent(orderId, transactionId, payType);
        event.setPayTime(System.currentTimeMillis());
        eventProducer.sendPaymentSuccess(event);

        log.info("支付成功并发送MQ事件: orderId={}, transactionId={}", orderId, transactionId);
    }

    /** 支付宝退款 */
    @Override
    @Transactional
    public boolean refund(String orderId, String subId, String payType, String fee) {
        log.info("退款请求: orderId={}, amount={}, payType={}", orderId, fee, payType);

        PaymentRecord record = paymentRecordMapper.findByOrderId(orderId);
        if (record == null || record.getTransactionId() == null) {
            log.warn("退款失败，未找到支付记录: orderId={}", orderId);
            return false;
        }

        if ("ALI_PAY".equals(payType) || "0".equals(payType)) {
            boolean success = aliPayClient.refund(orderId, record.getTransactionId(), fee, "订单退款");
            if (success) {
                record.setTradeStatus("REFUNDED");
                paymentRecordMapper.updateById(record);
            }
            return success;
        }

        // 微信退款
        if ("WX_PAY".equals(payType) || "WX_XCX_PAY".equals(payType) || "1".equals(payType) || "2".equals(payType)) {
            String totalFee = String.valueOf(record.getFee().multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_DOWN));
            boolean success = wxPayClient.refund(orderId, record.getTransactionId(), totalFee, totalFee);
            if (success) {
                record.setTradeStatus("REFUNDED");
                paymentRecordMapper.updateById(record);
            }
            return success;
        }

        log.warn("未知支付类型退款: payType={}", payType);
        return false;
    }
}
