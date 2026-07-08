package ny.shop.youxuan.marketingservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ny.shop.youxuan.marketingservice.entity.*;
import ny.shop.youxuan.marketingservice.service.MarketingService;
import java.util.*;

@Service
public class MarketingServiceImpl implements MarketingService {

    private static final Logger log = LoggerFactory.getLogger(MarketingServiceImpl.class);

    @Override public List<FlashSale> getActiveFlashSales() { return new ArrayList<>(); }
    @Override public FlashSale getFlashSale(String fsId) { return null; }
    @Override public List<UserCoupon> getUserCoupons(String uid) { return new ArrayList<>(); }

    @Override
    public boolean claimCoupon(String uid, String couponId) {
        log.info("领取优惠券: uid={}, couponId={}", uid, couponId);
        return true;
    }

    @Override
    public boolean useCoupon(String uid, String couponId, String orderId) {
        log.info("使用优惠券: uid={}, couponId={}, orderId={}", uid, couponId, orderId);
        return true;
    }

    @Override public List<RotationInfo> getActiveBanners() { return new ArrayList<>(); }
    @Override public GroupbuyInfo getGroupbuy(String groupId) { return null; }
    @Override public boolean joinGroup(String groupId, String uid) { return true; }
}
