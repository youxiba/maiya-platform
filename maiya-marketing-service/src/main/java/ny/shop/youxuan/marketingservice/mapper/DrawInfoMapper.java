package ny.shop.youxuan.marketingservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;import ny.shop.youxuan.marketingservice.entity.('DrawInfo','DrawInfo');

@Mapper
public interface DrawInfoMapper extends BaseMapper<('DrawInfo', 'DrawInfo')> {
    @Select("SELECT * FROM draw_info WHERE uid=#{u} ORDER BY draw_time DESC") java.util.List<DrawInfo> findByUid(@Param("u") String uid);
    @Select("SELECT * FROM draw_info WHERE activity_id=#{a} AND draw_status=#{s}") java.util.List<DrawInfo> findByActivityAndStatus(@Param("a") String aid, @Param("s") int status);
}