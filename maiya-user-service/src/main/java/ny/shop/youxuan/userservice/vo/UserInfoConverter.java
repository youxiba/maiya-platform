package ny.shop.youxuan.userservice.vo;

import ny.shop.youxuan.userservice.entity.UserInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息转换器
 *
 * Entity → VO 转换，隔离敏感字段。
 * 不使用工具自动映射，手动控制每个字段以确保安全。
 */
public class UserInfoConverter {

    private UserInfoConverter() {}

    /**
     * 转换为 app 端 VO（仅安全字段）
     */
    public static UserInfoVO toUserVO(UserInfo entity) {
        if (entity == null) return null;
        UserInfoVO vo = new UserInfoVO();
        vo.setUid(entity.getUid());
        vo.setUsername(entity.getUsername());
        vo.setNickname(entity.getNickname());
        vo.setAvatar(entity.getAvatar());
        vo.setTelephone(maskTelephone(entity.getTelephone()));
        vo.setInviteCode(entity.getInviteCode());
        vo.setLevel(entity.getLevel());
        vo.setRoles(entity.getRoles());
        vo.setCreateTime(entity.getCreateTime());
        vo.setEnable(entity.getEnable());
        return vo;
    }

    /**
     * 转换为管理端 VO（更多字段，仍排除密码/微信标识）
     */
    public static AdminUserInfoVO toAdminVO(UserInfo entity) {
        if (entity == null) return null;
        AdminUserInfoVO vo = new AdminUserInfoVO();
        vo.setUid(entity.getUid());
        vo.setUsername(entity.getUsername());
        vo.setNickname(entity.getNickname());
        vo.setRealname(entity.getRealname());
        vo.setAvatar(entity.getAvatar());
        vo.setWxNickname(entity.getWxNickname());
        vo.setWxAvatar(entity.getWxAvatar());
        vo.setTelephone(entity.getTelephone());
        vo.setInviteCode(entity.getInviteCode());
        vo.setSuperUid(entity.getSuperUid());
        vo.setCreatorUid(entity.getCreatorUid());
        vo.setMid(entity.getMid());
        vo.setLevel(entity.getLevel());
        vo.setRoles(entity.getRoles());
        vo.setCreateTime(entity.getCreateTime());
        vo.setRegtime(entity.getRegtime());
        vo.setEnable(entity.getEnable());
        return vo;
    }

    public static List<UserInfoVO> toUserVOList(List<UserInfo> list) {
        return list.stream().map(UserInfoConverter::toUserVO).collect(Collectors.toList());
    }

    public static List<AdminUserInfoVO> toAdminVOList(List<UserInfo> list) {
        return list.stream().map(UserInfoConverter::toAdminVO).collect(Collectors.toList());
    }

    /**
     * 手机号脱敏：13800001234 → 138****1234
     */
    private static String maskTelephone(String tel) {
        if (tel == null || tel.length() < 7) return tel;
        return tel.substring(0, 3) + "****" + tel.substring(tel.length() - 4);
    }
}
