package ny.shop.youxuan.merchantservice.service;

import ny.shop.youxuan.merchantservice.entity.MerchantHours;

public interface MerchantHoursService {
    MerchantHours getByUid(String uid);

    boolean isOpen(String mid);
}