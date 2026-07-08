package ny.shop.youxuan.deliveryservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.deliveryservice.entity.RiderInfo;

@Mapper
public interface RiderInfoMapper extends BaseMapper<RiderInfo> {
    @Select("SELECT * FROM rider_info WHERE rid=#{r} LIMIT 1")
    RiderInfo findByRid(@Param("r") String rid);
}