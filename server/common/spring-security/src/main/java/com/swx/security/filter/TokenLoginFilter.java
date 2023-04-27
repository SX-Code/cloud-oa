package com.swx.security.filter;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swx.common.jwt.JwtHelper;
import com.swx.common.pojo.R;
import com.swx.common.pojo.ResultCode;
import com.swx.common.utils.ResponseUtil;
import com.swx.security.custom.CustomUser;
import com.swx.vo.system.LoginVo;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 获得输入的用户名和密码，封装成框架要求的对象，调用认证方法
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final RedisTemplate<String, String> redisTemplate;
    // 构造方法
    public TokenLoginFilter(AuthenticationManager authenticationManager, RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        // 指定登陆接口及提交方式，可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login", "POST"));
    }

    // 登陆认证
    // 获取输入的用户名和密码，调用方法认证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 获取用户信息
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            // 封装对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            // 调用方法
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 认证成功调用的方法
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 获取当前用户
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        // 生成Token
        String token = JwtHelper.createToken(customUser.getSysUser().getId(), customUser.getSysUser().getUsername());
        // 获取当前用户的权限数据，放到Redis中，key: username  value: permissions
        redisTemplate.opsForValue().set(
                customUser.getUsername(),
                JSON.toJSONString(customUser.getAuthorities()));
        // 返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        ResponseUtil.out(response, R.success(map));
    }

    // 认证失败调用的方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        R r = R.fail(ResultCode.LOGIN_AUTH_FAIL);
        Throwable ex = failed.getCause();
        if (ex instanceof DisabledException) {
            r.setResultCode(ResultCode.USER_DISABLE);
        } else if (failed instanceof UsernameNotFoundException || failed instanceof BadCredentialsException) {
            r.setResultCode(ResultCode.USER_LOGIN_ERROR);
        }
        ResponseUtil.out(response, r);
    }
}
