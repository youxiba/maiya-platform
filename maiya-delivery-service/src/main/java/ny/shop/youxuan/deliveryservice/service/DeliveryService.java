package ny.shop.youxuan.deliveryservice.service;

import ny.shop.youxuan.deliveryservice.entity.DtbtInfo;

public interface DeliveryService {
    boolean createDelivery(DtbtInfo info);

    boolean updateStatus(String infoId, int status);
}