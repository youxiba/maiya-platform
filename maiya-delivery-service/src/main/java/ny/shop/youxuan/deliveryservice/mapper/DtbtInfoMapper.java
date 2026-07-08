package ny.shop.youxuan.deliveryservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.deliveryservice.entity.DtbtInfo;
import java.util.List;

@Mapper
public interface DtbtInfoMapper extends BaseMapper<DtbtInfo> {
    @Select("SELECT * FROM dtbt_info WHERE order_id=#{o}")
    List<DtbtInfo> findByOrderId(@Param("o") String oid);

    @Update("UPDATE dtbt_info SET dtbt_status=#{s} WHERE info_id=#{i}")
    int updateStatus(@Param("i") String id, @Param("s") int status);
}