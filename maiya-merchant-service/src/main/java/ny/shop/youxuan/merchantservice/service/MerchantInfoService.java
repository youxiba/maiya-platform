package ny.shop.youxuan.merchantservice.service;

import ny.shop.youxuan.merchantservice.entity.MerchantInfo;
import java.util.List;

public interface MerchantInfoService {
    MerchantInfo getByMid(String mid);

    MerchantInfo getByUid(String uid);

    String getUidByMid(String mid);

    List<MerchantInfo> findNearby(Double lon, Double lat, Integer range);

    boolean addMerchant(MerchantInfo m);

    boolean updateMerchant(MerchantInfo m);
}