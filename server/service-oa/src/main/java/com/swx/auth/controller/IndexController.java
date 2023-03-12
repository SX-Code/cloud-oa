package com.swx.auth.controller;

import com.swx.common.annotation.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 后台登陆管理 前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-03-05
 */
@Api(tags = "后台登陆管理")
@RestController
@ResponseResult
@RequestMapping("/admin/system/index")
public class IndexController {

    @ApiOperation("login请求，获取Token")
    @PostMapping("login")
    public Map<String, String> login() {
        HashMap<String, String> data = new HashMap<>();
        data.put("token", "admin-token");
        return data;
    }

    @ApiOperation("获取用户信息")
    @GetMapping("info")
    public Map<String, Object> info() {
        HashMap<String, Object> data = new HashMap<>();
        Map<String, String> permission1 = new HashMap<>();
        permission1.put("label", "主控台");
        permission1.put("value", "dashboard_console");
        Map<String, String> permission2 = new HashMap<>();
        permission2.put("label", "工作台");
        permission2.put("value", "dashboard_workplace");
        Map<String, String> permission3 = new HashMap<>();
        permission3.put("label", "角色管理");
        permission3.put("value", "system_role");
        Map<String, String> permission4 = new HashMap<>();
        permission4.put("label", "用户管理");
        permission4.put("value", "system_user");
        ArrayList<Map<String, String>> permissionList = new ArrayList<>();
        permissionList.add(permission1);
        permissionList.add(permission2);
        permissionList.add(permission3);
        permissionList.add(permission4);
        data.put("roles", "[admin]");
        data.put("name", "admin");
        data.put("permissions", permissionList);
        data.put("avatar", "https://cdn.staticaly.com/gh/sx-code/tuchuang@master/icon/avatar.jpeg");
        return data;
    }

    @ApiOperation("登出")
    @PostMapping("logout")
    public void logout() {
        // 删除缓存信息
        return;
    }

}
