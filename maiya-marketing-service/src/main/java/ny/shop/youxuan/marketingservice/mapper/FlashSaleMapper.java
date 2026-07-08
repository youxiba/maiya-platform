package ny.shop.youxuan.marketingservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;import ny.shop.youxuan.marketingservice.entity.('FlashSale','FlashSale');

@Mapper
public interface FlashSaleMapper extends BaseMapper<('FlashSale', 'FlashSale')> {
    @Select("SELECT * FROM flash_sale WHERE fs_id=#{f} LIMIT 1") FlashSale findByFsId(@Param("f") String fsId);
    @Select("SELECT * FROM flash_sale WHERE enable=1") java.util.List<FlashSale> findActive();
}