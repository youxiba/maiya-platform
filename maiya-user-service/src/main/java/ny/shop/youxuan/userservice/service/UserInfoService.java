package ny.shop.youxuan.userservice.service;

import ny.shop.youxuan.userservice.entity.UserInfo;

public interface UserInfoService {
    UserInfo getByUid(String uid);

    UserInfo getByTelephone(String t);

    UserInfo getByInviteCode(String c);

    UserInfo getByUnionId(String u);

    UserInfo getByQqid(String q);

    UserInfo getByAppleId(String a);

    boolean addUser(UserInfo u);

    boolean updateUser(UserInfo u);

    String getSuperUid(String uid);
}