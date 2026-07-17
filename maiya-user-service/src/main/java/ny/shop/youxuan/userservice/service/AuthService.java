package ny.shop.youxuan.userservice.service;

import com.alibaba.fastjson.JSONObject;
import ny.shop.youxuan.userservice.vo.LoginVO;

public interface AuthService {
    boolean sendPhoneCode(String telephone);

    JSONObject loginByPhone(String telephone, String code);

    LoginVO loginByPassword(String username, String password);

    JSONObject register(String telephone, String inviteCode);

    JSONObject thirdPartyLogin(String thirdType, String unionId, String inviteCode);

    String generateToken(String uid);

    String verifyToken(String token);
}