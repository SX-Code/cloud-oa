package com.swx.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.swx.auth.service.SysMenuService;
import com.swx.auth.service.SysUserService;
import com.swx.common.annotation.ResponseResult;
import com.swx.common.jwt.JwtHelper;
import com.swx.common.pojo.BizException;
import com.swx.common.utils.MD5;
import com.swx.model.system.SysUser;
import com.swx.vo.system.LoginVo;
import com.swx.vo.system.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    private final SysUserService sysUserService;
    private final SysMenuService sysMenuService;

    public IndexController(SysUserService sysUserService, SysMenuService sysMenuService) {
        this.sysUserService = sysUserService;
        this.sysMenuService = sysMenuService;
    }

    @ApiOperation("login请求，获取Token")
    @PostMapping("login")
    public Map<String, String> login(@RequestBody LoginVo loginVo) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, loginVo.getUsername());
        SysUser sysUser = sysUserService.getOne(wrapper);
        // 判断用户信息是否存在
        if (Objects.isNull(sysUser)) {
            throw new BizException("用户不存在");
        }
        // 判断密码
        String password = sysUser.getPassword();
        String encrypt = MD5.encrypt(loginVo.getPassword());
        if (!password.equals(encrypt)) {
            throw new BizException("密码错误");
        }
        // 判断用户是否被禁用
        if (sysUser.getStatus() == 0) {
            throw new BizException("用户已被禁用");
        }
        // 返回token
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }

    @ApiOperation("获取用户信息")
    @GetMapping("info")
    public SysUser info(HttpServletRequest request) {
        // 从token中获取用户信息(id)
        String token = request.getHeader("Authorization");
        Long userId = JwtHelper.getUserId(token);
        // 获取用户可操作的菜单列表
        SysUser sysUser = sysUserService.getById(userId);
        List<Permission> permissionList = sysMenuService.queryUserAuthListByUserId(userId);
        sysUser.setPermissions(permissionList);
        return sysUser;
    }

    @ApiOperation("登出")
    @PostMapping("logout")
    public void logout() {
        // 删除缓存信息
        return;
    }

}
