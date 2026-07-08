package ny.shop.youxuan.paymentservice.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface PaymentService {
    com.alibaba.fastjson.JSONObject unifiedPay(String orderId, String payType, Integer fee, String uid, String ip);

    String handleAliNotify(HttpServletRequest request, HttpServletResponse response);

    String handleWxNotify(HttpServletRequest request, HttpServletResponse response);

    String handleWxXcxNotify(HttpServletRequest request, HttpServletResponse response);

    boolean refund(String orderId, String subId, String payType, String fee);
}
