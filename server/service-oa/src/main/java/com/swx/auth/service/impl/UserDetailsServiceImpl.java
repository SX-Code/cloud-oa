package com.swx.auth.service.impl;

import com.swx.auth.service.SysMenuService;
import com.swx.auth.service.SysUserService;
import com.swx.model.system.SysUser;
import com.swx.security.custom.CustomUser;
import com.swx.security.custom.UserDetailsService;
import com.swx.vo.system.Permission;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserService sysUserService;
    private final SysMenuService sysMenuService;

    public UserDetailsServiceImpl(SysUserService sysUserService, SysMenuService sysMenuService) {
        this.sysUserService = sysUserService;
        this.sysMenuService = sysMenuService;
    }

    /**
     * 根据用户名获取用户对象，获取不到直接抛异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询
        SysUser sysUser = sysUserService.getUserByUsername(username);
        if (null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        if (sysUser.getStatus() == 0) {
            throw new DisabledException("disable");
        }

        // 查询权限列表
        List<Permission> permissions = sysMenuService.queryUserAuthListByUserId(sysUser.getId());
        // 封装Spring Security的权限类型
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        permissions.forEach(permission -> {
            authorities.add(new SimpleGrantedAuthority(permission.getAuth().trim()));
        });
        return new CustomUser(sysUser, authorities);
    }
}
