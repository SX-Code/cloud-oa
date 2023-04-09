package com.swx.auth.service;

import com.swx.model.system.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.vo.system.AssignMenuVo;
import com.swx.vo.system.MenuWithIdType;
import com.swx.vo.system.Permission;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-03-29
 */
public interface SysMenuService extends IService<SysMenu> {

    // 获取菜单list
    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    /**
     * 查询所有菜单和角色分配的菜单
     * @param roleId 角色id
     */
    List<SysMenu> findMenuByRoleId(Long roleId);

    /**
     * 角色分配菜单
     * @param assignMenuVo 菜单
     */
    void doAssign(AssignMenuVo assignMenuVo);

    /**
     * 查询角色分配的菜单ID和Type列表
     * @param roleId 角色id
     */
    List<MenuWithIdType> getMenuIdTypeList(Long roleId);

    List<Permission> queryUserAuthListByUserId(Long userId);

}
