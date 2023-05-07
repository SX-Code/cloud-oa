package com.swx.wechat.controller;


import com.swx.common.annotation.ResponseResult;
import com.swx.common.pojo.BizException;
import com.swx.model.wechat.Menu;
import com.swx.wechat.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-05-03
 */
@Api(tags = "微信公众号菜单")
@RestController
@ResponseResult
@RequestMapping("/admin/wechat/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @ApiOperation(value = "同步菜单")
    @PutMapping("sync")
    public void sync() throws WxErrorException {
        menuService.syncMenu();
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping("delete")
    public void delete() throws WxErrorException {
        menuService.removeMenu();
    }

    @ApiOperation(value = "获取全部菜单")
    @GetMapping()
    public List<Menu> list() {
        return menuService.listMenu();
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping()
    public void add(@RequestBody Menu menu) {
        boolean b = menuService.save(menu);
        if (!b) {
            throw new BizException("新增失败");
        }
    }

    @ApiOperation(value = "更新菜单")
    @PutMapping()
    public void update(@RequestBody Menu menu) {
        boolean b = menuService.updateById(menu);
        if (!b) {
            throw new BizException("更新失败");
        }
    }
}

