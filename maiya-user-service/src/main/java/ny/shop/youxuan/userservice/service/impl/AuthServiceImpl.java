package ny.shop.youxuan.userservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ny.shop.youxuan.common.dto.UserPermissionDTO;
import ny.shop.youxuan.common.exception.BizException;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.userservice.entity.SysMenu;
import ny.shop.youxuan.userservice.entity.UserInfo;
import ny.shop.youxuan.userservice.mapper.UserInfoMapper;
import ny.shop.youxuan.userservice.service.AuthService;
import ny.shop.youxuan.userservice.service.PermissionService;
import ny.shop.youxuan.userservice.service.SysMenuService;
import ny.shop.youxuan.userservice.vo.LoginVO;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private StringRedisTemplate redis;
    @Autowired
    private UserInfoMapper userMapper;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SysMenuService sysMenuService;

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration:86400000}")
    private long jwtExp;

    @Override
    public boolean sendPhoneCode(String telephone) {
        String code = String.valueOf((int) ((Math.random() * 9000) + 1000));
        redis.opsForValue().set("phone:code:" + telephone, code, 10, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public JSONObject loginByPhone(String telephone, String code) {
        String saved = redis.opsForValue().get("phone:code:" + telephone);
        if (saved == null || !saved.equals(code))
            throw new BizException(2004, "验证码错误");
        UserInfo user = userMapper.findByTelephone(telephone);
        if (user == null)
            throw new BizException(2002, "账号未注册");
        redis.delete("phone:code:" + telephone);
        return buildResult(user);
    }

    @Override
    @Transactional
    public JSONObject register(String telephone, String inviteCode) {
        if (userMapper.findByTelephone(telephone) != null)
            throw new BizException(2001, "已注册");
        UserInfo u = new UserInfo();
        u.setUid(UUID.randomUUID().toString().replace("-", ""));
        u.setTelephone(telephone);
        u.setUsername(telephone);
        u.setNickname(telephone);
        u.setRoles("ROLE_USER");
        u.setLevel(0);
        u.setEnable(true);
        u.setInviteCode(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        u.setCreateTime(System.currentTimeMillis());
        if (inviteCode != null && !inviteCode.isEmpty()) {
            UserInfo inviter = userMapper.findByInviteCode(inviteCode);
            if (inviter != null) {
                u.setSuperUid(inviter.getUid());
            }
        }
        userMapper.insert(u);
        return buildResult(u);
    }

    @Override
    public LoginVO loginByPassword(String username, String password) {
        // 1. 查询用户
        UserInfo user = userMapper.findByUsername(username);
        // 统一提示，防止用户名枚举攻击
        if (user == null) {
            throw new BizException(2002, "用户名或密码错误");
        }

        // 2. 校验密码（BCrypt + 站点盐值）
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password + "mkb", user.getPassword())) {
            throw new BizException(2004, "用户名或密码错误");
        }

        // 3. 生成 JWT Token
        String token = generateToken(user.getUid());

        // 4. 预缓存权限到 Redis
        permissionService.refreshUserPermissions(user.getUid());

        // 5. 组装安全返回结果（不含密码等敏感字段）
        return buildLoginVO(user, token);
    }

    @Override
    @Transactional
    public JSONObject thirdPartyLogin(String thirdType, String unionId, String inviteCode) {
        UserInfo u = null;
        switch (thirdType.toUpperCase()) {
            case "WECHAT":
                u = userMapper.findByUnionId(unionId);
                break;
            case "QQ":
                u = userMapper.findByQqid(unionId);
                break;
            case "APPLE":
                u = userMapper.findByAppleId(unionId);
                break;
        }
        if (u == null) {
            u = new UserInfo();
            u.setUid(UUID.randomUUID().toString().replace("-", ""));
            u.setRoles("ROLE_USER");
            u.setLevel(0);
            u.setEnable(true);
            u.setInviteCode(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
            if ("WECHAT".equals(thirdType.toUpperCase()))
                u.setUnionId(unionId);
            else if ("QQ".equals(thirdType.toUpperCase()))
                u.setQqid(unionId);
            else if ("APPLE".equals(thirdType.toUpperCase()))
                u.setAppleId(unionId);
            if (inviteCode != null && !inviteCode.isEmpty()) {
                UserInfo inv = userMapper.findByInviteCode(inviteCode);
                if (inv != null)
                    u.setSuperUid(inv.getUid());
            }
            userMapper.insert(u);
        }
        return buildResult(u);
    }

    @Override
    public String generateToken(String uid) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().subject(uid).issuer("maiya-auth").issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExp)).signWith(key).compact();
    }

    @Override
    public String verifyToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 构建安全登录响应（LoginVO），不含密码等敏感字段
     */
    private LoginVO buildLoginVO(UserInfo user, String token) {
        LoginVO vo = new LoginVO();
        vo.setToken("Bearer " + token);
        vo.setUid(user.getUid());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());

        // 从缓存/DB 加载角色和权限列表
        UserPermissionDTO perm = permissionService.getUserPermissions(user.getUid());
        vo.setRoles(perm.getRoles());
        vo.setPermissions(perm.getPermissions());

        // 获取权限过滤后的菜单树
        ApiResult<List<SysMenu>> menuResult = sysMenuService.getUserMenus(user.getUid());
        if (menuResult != null && menuResult.getData() != null) {
            vo.setMenus(menuResult.getData());
        }
        return vo;
    }

    /**
     * 构建旧版 JSON 响应（兼容手机号/注册/三方登录，后续逐步迁移至 LoginVO）
     */
    private JSONObject buildResult(UserInfo u) {
        // 登录成功 → 预缓存用户权限
        permissionService.refreshUserPermissions(u.getUid());

        JSONObject r = new JSONObject();
        r.put("uid", u.getUid());
        r.put("username", u.getUsername());
        r.put("nickname", u.getNickname());
        r.put("avatar", u.getAvatar());
        r.put("level", u.getLevel());
        r.put("token", "Bearer " + generateToken(u.getUid()));
        return r;
    }
}