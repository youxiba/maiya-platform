package ny.shop.youxuan.userservice.service;

import com.alibaba.fastjson.JSONObject;

public interface AuthService {
    boolean sendPhoneCode(String telephone);

    JSONObject loginByPhone(String telephone, String code);

    JSONObject register(String telephone, String inviteCode);

    JSONObject thirdPartyLogin(String thirdType, String unionId, String inviteCode);

    String generateToken(String uid);

    String verifyToken(String token);
}