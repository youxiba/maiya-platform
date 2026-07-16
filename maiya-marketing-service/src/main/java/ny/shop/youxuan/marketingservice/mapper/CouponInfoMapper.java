package ny.shop.youxuan.marketingservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.marketingservice.entity.CouponInfo;

@Mapper
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {
    @Select("SELECT * FROM coupon_info WHERE coupon_id=#{c} LIMIT 1")
    CouponInfo findByCouponId(@Param("c") String cid);

    @Select("SELECT * FROM coupon_info WHERE end_time<#{n} AND valid=1")
    java.util.List<CouponInfo> findExpired(@Param("n") long now);
}