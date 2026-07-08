package ny.shop.youxuan.merchantservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.merchantservice.entity.MerchantInfo;
import java.util.List;

@Mapper
public interface MerchantInfoMapper extends BaseMapper<MerchantInfo> {
    @Select("SELECT * FROM merchant_info WHERE mid=#{m} LIMIT 1")
    MerchantInfo findByMid(@Param("m") String mid);

    @Select("SELECT * FROM merchant_info WHERE uid=#{u} LIMIT 1")
    MerchantInfo findByUid(@Param("u") String uid);

    @Select("SELECT uid FROM merchant_info WHERE mid=#{m} LIMIT 1")
    String getUidByMid(@Param("m") String mid);

    @Select("SELECT *, ROUND(ST_Distance_Sphere(POINT(lon,lat),POINT(#{lon},#{lat}))) AS distance FROM merchant_info WHERE online=1 AND audit_status=2 AND ROUND(ST_Distance_Sphere(POINT(lon,lat),POINT(#{lon},#{lat}))) <= #{r} ORDER BY distance ASC")
    List<MerchantInfo> findNearby(@Param("lon") Double lon, @Param("lat") Double lat, @Param("r") Integer range);
}