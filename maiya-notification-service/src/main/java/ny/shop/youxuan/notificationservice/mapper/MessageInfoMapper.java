package ny.shop.youxuan.notificationservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.notificationservice.entity.MessageInfo;
import java.util.List;

@Mapper
public interface MessageInfoMapper extends BaseMapper<MessageInfo> {
    @Select("SELECT * FROM message_info WHERE uid=#{u} ORDER BY create_time DESC")
    List<MessageInfo> findByUid(@Param("u") String uid);
}