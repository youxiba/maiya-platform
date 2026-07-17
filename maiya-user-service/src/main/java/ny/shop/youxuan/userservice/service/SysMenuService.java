package ny.shop.youxuan.userservice.service;

import ny.shop.youxuan.common.dto.UserPermissionDTO;
import ny.shop.youxuan.common.result.ApiResult;
import ny.shop.youxuan.userservice.entity.SysMenu;
import ny.shop.youxuan.userservice.mapper.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统菜单服务
 *
 * 根据用户权限返回可见的菜单树。
 * 前端根据 perm_code 过滤菜单，有权限的才展示。
 */
@Service
public class SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取用户的菜单树
     *
     * @param userId 用户 ID
     * @return 树形结构的菜单列表
     */
    public ApiResult<List<SysMenu>> getUserMenus(String userId) {
        UserPermissionDTO perm = permissionService.getUserPermissions(userId);

        // 查询所有启用的菜单
        List<SysMenu> allMenus = sysMenuMapper.findAllEnabled();

        // 按权限过滤
        List<SysMenu> filtered = allMenus.stream()
                .filter(m -> hasPermission(perm, m))
                .collect(Collectors.toList());

        // 构建树形结构
        List<SysMenu> tree = buildTree(filtered);

        return ApiResult.success(tree);
    }

    /**
     * 获取所有菜单（管理员用）
     */
    public ApiResult<List<SysMenu>> getAllMenus() {
        List<SysMenu> all = sysMenuMapper.findAllEnabled();
        return ApiResult.success(buildTree(all));
    }

    // ========================================================================
    // 私有方法
    // ========================================================================

    /** 判断用户是否有权访问该菜单 */
    private boolean hasPermission(UserPermissionDTO perm, SysMenu menu) {
        String permCode = menu.getPermCode();
        if (permCode == null || permCode.isEmpty()) {
            return true; // 无权限限制
        }
        // 超级管理员拥有所有权限
        if (perm.getRoles().contains("ROLE_SUPERADMIN")) {
            return true;
        }
        // 检查用户的角色是否有对应权限
        return perm.getPermissions().contains(permCode);
    }

    /** 构建树形结构 */
    private List<SysMenu> buildTree(List<SysMenu> flatList) {
        List<SysMenu> roots = new ArrayList<>();
        for (SysMenu menu : flatList) {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                roots.add(menu);
                fillChildren(menu, flatList);
            }
        }
        return roots;
    }

    private void fillChildren(SysMenu parent, List<SysMenu> all) {
        List<SysMenu> children = new ArrayList<>();
        for (SysMenu menu : all) {
            if (parent.getId().equals(menu.getParentId())) {
                children.add(menu);
                fillChildren(menu, all);
            }
        }
        parent.setChildren(children);
    }
}
