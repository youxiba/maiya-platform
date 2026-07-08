package ny.shop.youxuan.marketingservice.service;

import ny.shop.youxuan.marketingservice.entity.*;
import java.util.List;

/** 营销活动服务 */
public interface MarketingService {

    // ===== 秒杀 =====
    List<FlashSale> getActiveFlashSales();
    FlashSale getFlashSale(String fsId);

    // ===== 优惠券 =====
    List<UserCoupon> getUserCoupons(String uid);
    boolean claimCoupon(String uid, String couponId);
    boolean useCoupon(String uid, String couponId, String orderId);

    // ===== 活动Banner =====
    List<RotationInfo> getActiveBanners();

    // ===== 拼团 =====
    GroupbuyInfo getGroupbuy(String groupId);
    boolean joinGroup(String groupId, String uid);
}
