package ny.shop.youxuan.paymentservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.paymentservice.entity.PaymentRecord;

@Mapper
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {
    @Select("SELECT * FROM payment_record WHERE transaction_id=#{t} LIMIT 1")
    PaymentRecord findByTransactionId(@Param("t") String tid);

    @Select("SELECT * FROM payment_record WHERE order_id=#{o} LIMIT 1")
    PaymentRecord findByOrderId(@Param("o") String oid);
}