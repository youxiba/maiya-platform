package ny.shop.youxuan.financeservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.financeservice.entity.WalletInfo;
import java.math.BigDecimal;

@Mapper
public interface WalletInfoMapper extends BaseMapper<WalletInfo> {
    @Select("SELECT * FROM wallet_info WHERE uid=#{u} LIMIT 1")
    WalletInfo findByUid(@Param("u") String uid);

    @Update("UPDATE wallet_info SET money=money+#{a} WHERE uid=#{u}")
    int addMoney(@Param("u") String uid, @Param("a") BigDecimal amount);
}