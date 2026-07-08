package ny.shop.youxuan.orderservice.service;

import ny.shop.youxuan.orderservice.entity.OrderInfo;
import ny.shop.youxuan.orderservice.statemachine.OrderEvent;
import ny.shop.youxuan.orderservice.statemachine.OrderStatus;

public interface OrderService {
    String createOrder(OrderInfo order);

    boolean processPayment(String orderId, String transactionId, Integer payType);

    OrderInfo getByOrderId(String orderId);

    java.util.List<OrderInfo> getUserOrders(String uid);

    int closeTimeoutOrders();
}