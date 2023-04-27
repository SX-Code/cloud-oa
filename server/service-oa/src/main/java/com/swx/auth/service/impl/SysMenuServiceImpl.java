package com.swx.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.swx.auth.service.SysRoleMenuService;
import com.swx.auth.utils.MenuHelper;
import com.swx.common.pojo.BizException;
import com.swx.model.system.SysMenu;
import com.swx.auth.mapper.SysMenuMapper;
import com.swx.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.model.system.SysRoleMenu;
import com.swx.vo.system.AssignMenuVo;
import com.swx.vo.system.MenuWithIdType;
import com.swx.vo.system.Permission;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-03-29
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysRoleMenuService sysRoleMenuService;

    public SysMenuServiceImpl(SysRoleMenuService sysRoleMenuService) {
        this.sysRoleMenuService = sysRoleMenuService;
    }

    @Override
    public List<SysMenu> findNodes() {
        // 查询所有菜单数据
        List<SysMenu> sysMenus = baseMapper.selectList(null);

        // 构建树形结构
        List<SysMenu> resultList = MenuHelper.buildTree(sysMenus);
        return resultList;
    }

    @Override
    public void removeMenuById(Long id) {
        // 判断当前菜单是否有子菜单
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BizException("子菜单不能删除");
        }
        baseMapper.deleteById(id);
    }

    /**
     * 查询所有菜单和角色分配的菜单
     *
     * @param roleId 角色id
     */
    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        // 查询所有可用菜单，status == 1
        LambdaQueryWrapper<SysMenu> wrapperSysMenu = new LambdaQueryWrapper<>();
        wrapperSysMenu.eq(SysMenu::getStatus, 1);
        List<SysMenu> allSysMenus = baseMapper.selectList(wrapperSysMenu);
        // 查询用户分配的菜单
        LambdaQueryWrapper<SysRoleMenu> wrapperSysRoleMenu = new LambdaQueryWrapper<>();
        wrapperSysRoleMenu.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(wrapperSysRoleMenu);
        // 根据获取的菜单id，获取对应菜单对象
        List<Long> menuIdList = sysRoleMenuList.stream().map(c -> c.getMenuId()).collect(Collectors.toList());
        allSysMenus.stream().forEach(item -> {
            item.setIsSelect(menuIdList.contains(item.getId()));
        });
        // 构建树形菜单
        List<SysMenu> sysMenus = MenuHelper.buildTree(allSysMenus);
        return sysMenus;
    }

    /**
     * 角色分配菜单
     *
     * @param assignMenuVo 菜单
     */
    @Override
    public void doAssign(AssignMenuVo assignMenuVo) {
        // 根据角色ID删除菜单角色表
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, assignMenuVo.getRoleId());
        sysRoleMenuService.remove(wrapper);

        // 从参数中获取角色新分配菜单id列表
        List<Long> menuIdList = assignMenuVo.getMenuIdList();
        for (Long menuId : menuIdList) {
            if (StringUtils.isEmpty(menuId)) {
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
            sysRoleMenuService.save(sysRoleMenu);
        }
    }

    /**
     * 查询角色分配的菜单ID和Type列表
     *
     * @param roleId 角色id
     */
    @Override
    public List<MenuWithIdType> getMenuIdTypeList(Long roleId) {
        return baseMapper.selectIdTypeList(roleId);
    }

    @Override
    public List<Permission> queryUserAuthListByUserId(Long userId) {
        // 关联查询用户的权限
        return baseMapper.queryAuthListByUserId(userId);
    }
}
