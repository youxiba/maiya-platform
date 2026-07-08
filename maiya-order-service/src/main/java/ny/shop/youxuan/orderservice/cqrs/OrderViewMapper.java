package ny.shop.youxuan.orderservice.cqrs;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface OrderViewMapper extends BaseMapper<OrderView> {
    @Select("SELECT * FROM order_view WHERE uid=#{u} ORDER BY create_time DESC")
    List<OrderView> findByUid(@Param("u") String uid);

    @Select("SELECT * FROM order_view WHERE order_id=#{o} LIMIT 1")
    OrderView findByOrderId(@Param("o") String oid);
}