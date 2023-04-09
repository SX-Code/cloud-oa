package com.swx.auth.utils;

import com.swx.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuHelper {
    // 使用递归方法构建菜单
    public static List<SysMenu> buildTree(List<SysMenu> sysMenus) {
        // 创建list集合，用于存储最终数据
        List<SysMenu> trees = new ArrayList<>();
        // 把所有的菜单数据进行遍历
        for (SysMenu sysMenu : sysMenus) {
            // 递归入口，parentId == 0
            if (sysMenu.getParentId() == 0) {
                    trees.add(getChildren(sysMenu, sysMenus));
            }
        }
        return trees;
    }

    public static SysMenu getChildren(SysMenu sysMenu, List<SysMenu> sysMenus) {
        sysMenu.setChildren(new ArrayList<>());
        // 遍历所有菜单数据
        for (SysMenu item : sysMenus) {
            if (Objects.equals(sysMenu.getId(), item.getParentId())) {
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(getChildren(item, sysMenus));
            }
        }
        return sysMenu;
    }
}
