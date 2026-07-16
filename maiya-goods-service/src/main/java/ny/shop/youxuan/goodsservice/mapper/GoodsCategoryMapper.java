package ny.shop.youxuan.goodsservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.goodsservice.entity.GoodsCategory;
import java.util.List;

@Mapper
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategory> {
    @Select("SELECT * FROM goods_category ORDER BY sort ASC")
    List<GoodsCategory> findAllOrdered();
}
