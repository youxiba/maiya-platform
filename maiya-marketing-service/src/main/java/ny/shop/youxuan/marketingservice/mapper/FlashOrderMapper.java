package ny.shop.youxuan.marketingservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.marketingservice.entity.FlashOrder;
import java.util.List;

@Mapper
public interface FlashOrderMapper extends BaseMapper<FlashOrder> {

    @Select("SELECT * FROM flash_order WHERE uid = #{uid} AND fs_id = #{fsId} AND order_status = 0 LIMIT 1")
    FlashOrder findPendingOrder(@Param("uid") String uid, @Param("fsId") String fsId);

    @Select("SELECT * FROM flash_order WHERE order_status = 0 AND create_time < #{deadline}")
    List<FlashOrder> findTimeoutOrders(@Param("deadline") Long deadline);

    @Update("UPDATE flash_order SET order_status = #{status} WHERE order_id = #{orderId}")
    int updateStatus(@Param("orderId") String orderId, @Param("status") Integer status);

    @Select("SELECT COUNT(*) FROM flash_order WHERE uid = #{uid} AND fs_id = #{fsId}")
    int countByUser(@Param("uid") String uid, @Param("fsId") String fsId);
}
