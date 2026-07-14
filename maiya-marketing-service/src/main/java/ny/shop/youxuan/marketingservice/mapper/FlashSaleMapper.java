package ny.shop.youxuan.marketingservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.marketingservice.entity.FlashSale;

import java.util.List;

@Mapper
public interface FlashSaleMapper extends BaseMapper<FlashSale> {

    @Select("SELECT * FROM flash_sale WHERE fs_id=#{f} LIMIT 1")
    FlashSale findByFsId(@Param("f") String fsId);

    @Select("SELECT * FROM flash_sale WHERE enable=1")
    List<FlashSale> findActive();

    /**
     * 扣减真实库存（乐观锁）
     * 只在 total_stock - sold_stock >= qty 时更新
     *
     * @return 影响行数（0=库存不足）
     */
    @Update(
        "UPDATE flash_sale " +
        "SET sold_stock = sold_stock + #{qty}, " +
        "    version = version + 1 " +
        "WHERE fs_id = #{fsId} " +
        "  AND (total_stock - sold_stock) >= #{qty}"
    )
    int deductStock(@Param("fsId") String fsId, @Param("qty") int qty);
}
