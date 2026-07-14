package ny.shop.youxuan.marketingservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.marketingservice.entity.FlashOrder;

import java.util.List;

@Mapper
public interface FlashOrderMapper extends BaseMapper<FlashOrder> {

    /**
     * 唯一键防重插入
     * INSERT ... ON DUPLICATE KEY UPDATE
     * 利用 (uid, fs_id) 唯一键防重
     */
    @Insert(
        "INSERT INTO flash_order (request_id, order_id, fs_id, uid, goods_info_id, " +
        "flash_price, qty, order_status, create_time) " +
        "VALUES (#{requestId}, #{orderId}, #{fsId}, #{uid}, #{goodsInfoId}, " +
        "#{flashPrice}, #{qty}, #{orderStatus}, #{createTime}) " +
        "ON DUPLICATE KEY UPDATE " +
        "request_id = VALUES(request_id), " +
        "order_status = IF(order_status = 1, order_status, VALUES(order_status))"
    )
    int insertWithUniqueCheck(FlashOrder order);

    /**
     * 批量 INSERT IGNORE（批量场景使用）
     * 忽略重复行，不影响其他行
     */
    @Insert(
        "<script>" +
        "INSERT IGNORE INTO flash_order (request_id, order_id, fs_id, uid, goods_info_id, " +
        "flash_price, qty, order_status, create_time) VALUES " +
        "<foreach collection='list' item='o' separator=','>" +
        "(#{o.requestId}, #{o.orderId}, #{o.fsId}, #{o.uid}, #{o.goodsInfoId}, " +
        "#{o.flashPrice}, #{o.qty}, #{o.orderStatus}, #{o.createTime})" +
        "</foreach>" +
        "</script>"
    )
    int batchInsertIgnore(@Param("list") List<FlashOrder> orders);

    /**
     * 按 requestId 查询（消息幂等）
     */
    @Select("SELECT * FROM flash_order WHERE request_id = #{requestId} LIMIT 1")
    FlashOrder findByRequestId(@Param("requestId") String requestId);

    @Select("SELECT * FROM flash_order WHERE uid = #{uid} AND fs_id = #{fsId} AND order_status = 0 LIMIT 1")
    FlashOrder findPendingOrder(@Param("uid") String uid, @Param("fsId") String fsId);

    @Select("SELECT * FROM flash_order WHERE order_status = 0 AND create_time < #{deadline}")
    List<FlashOrder> findTimeoutOrders(@Param("deadline") Long deadline);

    @Update("UPDATE flash_order SET order_status = #{status} WHERE order_id = #{orderId}")
    int updateStatus(@Param("orderId") String orderId, @Param("status") Integer status);

    @Select("SELECT COUNT(*) FROM flash_order WHERE uid = #{uid} AND fs_id = #{fsId}")
    int countByUser(@Param("uid") String uid, @Param("fsId") String fsId);
}
