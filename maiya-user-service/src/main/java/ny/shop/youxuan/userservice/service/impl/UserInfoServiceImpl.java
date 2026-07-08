package ny.shop.youxuan.userservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ny.shop.youxuan.userservice.entity.UserInfo;
import ny.shop.youxuan.userservice.mapper.UserInfoMapper;
import ny.shop.youxuan.userservice.service.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper mapper;

    @Override
    public UserInfo getByUid(String uid) {
        return mapper.findByUid(uid);
    }

    @Override
    public UserInfo getByTelephone(String t) {
        return mapper.findByTelephone(t);
    }

    @Override
    public UserInfo getByInviteCode(String c) {
        return mapper.findByInviteCode(c);
    }

    @Override
    public UserInfo getByUnionId(String u) {
        return mapper.findByUnionId(u);
    }

    @Override
    public UserInfo getByQqid(String q) {
        return mapper.findByQqid(q);
    }

    @Override
    public UserInfo getByAppleId(String a) {
        return mapper.findByAppleId(a);
    }

    @Override
    public boolean addUser(UserInfo u) {
        return mapper.insert(u) > 0;
    }

    @Override
    public boolean updateUser(UserInfo u) {
        return mapper.updateById(u) > 0;
    }

    @Override
    public String getSuperUid(String uid) {
        return mapper.getSuperUid(uid);
    }
}