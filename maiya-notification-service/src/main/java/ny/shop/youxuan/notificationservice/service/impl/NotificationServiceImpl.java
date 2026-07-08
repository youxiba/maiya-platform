package ny.shop.youxuan.notificationservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ny.shop.youxuan.notificationservice.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public boolean sendMessage(String uid, String title, String text) {
        log.info("send msg: {} {} {}", uid, title, text);
        return true;
    }

    @Override
    public boolean printTicket(String orderId, String mid) {
        log.info("print: {} for {}", orderId, mid);
        return true;
    }
}