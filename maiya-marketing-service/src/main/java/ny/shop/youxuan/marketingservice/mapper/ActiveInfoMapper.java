package ny.shop.youxuan.marketingservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.marketingservice.entity.ActiveInfo;

@Mapper
public interface ActiveInfoMapper extends BaseMapper<ActiveInfo> {
    @Select("SELECT * FROM active_info WHERE active_id=#{a} LIMIT 1")
    ActiveInfo findByActiveId(@Param("a") String aid);

    @Select("SELECT * FROM active_info WHERE active_type=#{t} AND active_status=#{s} AND end_time<#{n}")
    java.util.List<ActiveInfo> findExpired(@Param("t") int type, @Param("s") int status, @Param("n") long now);
}