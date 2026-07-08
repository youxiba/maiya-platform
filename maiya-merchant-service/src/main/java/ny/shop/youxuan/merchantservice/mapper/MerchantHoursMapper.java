package ny.shop.youxuan.merchantservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.merchantservice.entity.MerchantHours;

@Mapper
public interface MerchantHoursMapper extends BaseMapper<MerchantHours> {
    @Select("SELECT * FROM merchant_hours WHERE uid=#{u} LIMIT 1")
    MerchantHours findByUid(@Param("u") String uid);

    @Select("SELECT mh.* FROM merchant_hours mh INNER JOIN merchant_info mi ON mh.uid=mi.uid WHERE mi.mid=#{m} LIMIT 1")
    MerchantHours findByMid(@Param("m") String mid);
}