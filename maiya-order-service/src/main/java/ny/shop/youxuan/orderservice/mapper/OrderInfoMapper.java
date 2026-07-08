package ny.shop.youxuan.orderservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.orderservice.entity.OrderInfo;
import java.util.List;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    @Select("SELECT * FROM order_info WHERE order_id=#{o}")
    List<OrderInfo> findByOrderId(@Param("o") String oid);

    @Select("SELECT * FROM order_info WHERE uid=#{u} ORDER BY create_time DESC")
    List<OrderInfo> findByUid(@Param("u") String uid);

    @Select("SELECT * FROM order_info WHERE order_status=#{s} AND create_time<#{t}")
    List<OrderInfo> findTimeoutOrders(@Param("s") int status, @Param("t") long time);

    @Update("UPDATE order_info SET order_status=#{s},pay_type=#{p},transaction_id=#{t},pay_time=#{n} WHERE order_id=#{o}")
    int paySuccess(@Param("o") String oid, @Param("s") int s, @Param("p") int p, @Param("t") String t,
            @Param("n") long n);

    @Update("UPDATE order_info SET order_status=#{ns} WHERE order_id=#{o} AND order_status=#{os}")
    int updateStatus(@Param("o") String oid, @Param("os") int os, @Param("ns") int ns);
}