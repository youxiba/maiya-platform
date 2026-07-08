package ny.shop.youxuan.orderservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ny.shop.youxuan.orderservice.entity.OrderInfo;
import ny.shop.youxuan.orderservice.mapper.OrderInfoMapper;
import ny.shop.youxuan.orderservice.service.OrderService;
import ny.shop.youxuan.orderservice.statemachine.OrderStatus;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderInfoMapper mapper;

    @Override
    @Transactional
    public String createOrder(OrderInfo order) {
        if (order.getOrderId() == null)
            order.setOrderId(UUID.randomUUID().toString().replace("-", ""));
        order.setOrderStatus(OrderStatus.ORDER.getCode());
        order.setCreateTime(System.currentTimeMillis());
        mapper.insert(order);
        return order.getOrderId();
    }

    @Override
    @Transactional
    public boolean processPayment(String orderId, String transactionId, Integer payType) {
        List<OrderInfo> orders = mapper.findByOrderId(orderId);
        if (orders.isEmpty())
            return false;
        OrderInfo order = orders.get(0);
        if (order.getOrderStatus() != OrderStatus.ORDER.getCode())
            return false;
        mapper.paySuccess(orderId, OrderStatus.PAID.getCode(), payType, transactionId, System.currentTimeMillis());
        log.info("支付回调处理成功: orderId={}", orderId);
        return true;
    }

    @Override
    public OrderInfo getByOrderId(String orderId) {
        List<OrderInfo> list = mapper.findByOrderId(orderId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<OrderInfo> getUserOrders(String uid) {
        return mapper.findByUid(uid);
    }

    @Override
    @Transactional
    public int closeTimeoutOrders() {
        long timeout = System.currentTimeMillis() - 30 * 60 * 1000L;
        List<OrderInfo> orders = mapper.findTimeoutOrders(OrderStatus.ORDER.getCode(), timeout);
        for (OrderInfo o : orders) {
            mapper.updateStatus(o.getOrderId(), OrderStatus.ORDER.getCode(), OrderStatus.USELESS.getCode());
        }
        return orders.size();
    }
}