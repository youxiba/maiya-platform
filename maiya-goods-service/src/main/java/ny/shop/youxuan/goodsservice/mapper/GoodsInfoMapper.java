package ny.shop.youxuan.goodsservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.goodsservice.entity.GoodsInfo;
import java.util.List;

@Mapper
public interface GoodsInfoMapper extends BaseMapper<GoodsInfo> {
    @Select("SELECT * FROM goods_info WHERE goods_info_id = #{id} LIMIT 1")
    GoodsInfo findByGoodsInfoId(@Param("id") String id);

    @Select("SELECT * FROM goods_info WHERE mid = #{mid}")
    List<GoodsInfo> findByMid(@Param("mid") String mid);

    @Select("SELECT * FROM goods_info WHERE mid = #{mid} AND sta_flag = 1")
    List<GoodsInfo> findOnSaleByMid(@Param("mid") String mid);

    @Update("UPDATE goods_info SET sum = sum - #{qty} WHERE goods_info_id = #{id} AND sum >= #{qty}")
    int deductStock(@Param("id") String id, @Param("qty") Integer qty);

    @Update("UPDATE goods_info SET sum = sum + #{qty} WHERE goods_info_id = #{id}")
    int rollbackStock(@Param("id") String id, @Param("qty") Integer qty);

    @Update("UPDATE goods_info SET sales = sales + #{qty} WHERE goods_info_id = #{id}")
    int addSales(@Param("id") String id, @Param("qty") Integer qty);
}
