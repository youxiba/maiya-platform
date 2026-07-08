package ny.shop.youxuan.notificationservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.notificationservice.entity.PrinterInfo;
import java.util.List;

@Mapper
public interface PrinterInfoMapper extends BaseMapper<PrinterInfo> {
    @Select("SELECT * FROM printer_info WHERE mid=#{m} AND auto=1 AND enable=1")
    List<PrinterInfo> findAutoByMid(@Param("m") String mid);
}