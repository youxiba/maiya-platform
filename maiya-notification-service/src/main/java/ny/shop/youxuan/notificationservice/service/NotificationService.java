package ny.shop.youxuan.notificationservice.service;

public interface NotificationService {
    boolean sendMessage(String uid, String title, String text);

    boolean printTicket(String orderId, String mid);
}