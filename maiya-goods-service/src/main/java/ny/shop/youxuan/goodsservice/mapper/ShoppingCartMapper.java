package ny.shop.youxuan.goodsservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.goodsservice.entity.ShoppingCart;
import java.util.List;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    @Select("SELECT * FROM shopping_cart WHERE uid = #{uid} ORDER BY create_time DESC")
    List<ShoppingCart> findByUid(@Param("uid") String uid);
}
