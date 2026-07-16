package ny.shop.youxuan.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import ny.shop.youxuan.userservice.entity.UserInfo;
import java.util.List;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    @Select("SELECT * FROM user_info WHERE telephone=#{t} LIMIT 1")
    UserInfo findByTelephone(@Param("t") String t);

    @Select("SELECT * FROM user_info WHERE uid=#{u} LIMIT 1")
    UserInfo findByUid(@Param("u") String u);

    @Select("SELECT * FROM user_info WHERE invite_code=#{c} LIMIT 1")
    UserInfo findByInviteCode(@Param("c") String c);

    @Select("SELECT * FROM user_info WHERE union_id=#{u} LIMIT 1")
    UserInfo findByUnionId(@Param("u") String u);

    @Select("SELECT * FROM user_info WHERE qqid=#{q} LIMIT 1")
    UserInfo findByQqid(@Param("q") String q);

    @Select("SELECT * FROM user_info WHERE apple_id=#{a} LIMIT 1")
    UserInfo findByAppleId(@Param("a") String a);

    @Select("SELECT * FROM user_info WHERE username=#{u} LIMIT 1")
    UserInfo findByUsername(@Param("u") String username);

    @Select("SELECT super_uid FROM user_info WHERE uid=#{u} LIMIT 1")
    String getSuperUid(@Param("u") String u);
}