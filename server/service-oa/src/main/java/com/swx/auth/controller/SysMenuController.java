package com.swx.auth.controller;


import com.swx.auth.service.SysMenuService;
import com.swx.common.annotation.ResponseResult;
import com.swx.common.pojo.BizException;
import com.swx.model.system.SysMenu;
import com.swx.vo.system.AssignMenuVo;
import com.swx.vo.system.MenuWithIdType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-03-29
 */
@Api(tags = "菜单管理")
@ResponseResult
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @ApiOperation("查询所有菜单和角色分配的菜单")
    @GetMapping("toAssign/{roleId}")
    public List<SysMenu> toAssign(@PathVariable Long roleId) {
        return sysMenuService.findMenuByRoleId(roleId);
    }

    @ApiOperation("查询角色分配的菜单ID和Type列表")
    @GetMapping("menuIdType/{roleId}")
    public List<MenuWithIdType> getRoleMenuIdTypeList(@PathVariable Long roleId) {
        return sysMenuService.getMenuIdTypeList(roleId);
    }


    @ApiOperation("角色分配菜单")
    @PostMapping("doAssign")
    public void toAssign(@RequestBody AssignMenuVo assignMenuVo) {
        sysMenuService.doAssign(assignMenuVo);
    }

    @ApiOperation(value = "获取菜单")
    @GetMapping("findNodes")
    public List<SysMenu> findNodes() {
        return sysMenuService.findNodes();
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("")
    public void save(@RequestBody SysMenu sysMenu) {
        boolean save = sysMenuService.updateById(sysMenu);
        if (!save) {
            throw new BizException("菜单新增失败");
        }
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("")
    public void updateById(@RequestBody SysMenu sysMenu) {
        boolean update = sysMenuService.updateById(sysMenu);
        if (!update) {
            throw new BizException("菜单修改失败");
        }
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping("{id}")
    public void remove(@PathVariable Long id) {
        sysMenuService.removeMenuById(id);
    }

}

