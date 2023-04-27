package com.swx.security.filter;

import com.alibaba.fastjson2.JSON;
import com.swx.common.jwt.JwtHelper;
import com.swx.common.pojo.R;
import com.swx.common.pojo.ResultCode;
import com.swx.common.utils.ResponseUtil;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 判断是否完成认证
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 如果是登陆接口，直接放行
        if ("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            if (null != authentication) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            } else {
                ResponseUtil.out(response, R.fail(ResultCode.LOGIN_AUTH_FAIL));
            }
        } catch (JwtException e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            R r = R.fail(ResultCode.TOKEN_INVALID);
            if (e instanceof UnsupportedJwtException) {
                r.setResultCode(ResultCode.TOKEN_UNSUPPORTED);
            }
            ResponseUtil.out(response, r);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(token)) {
            String username = JwtHelper.getUsername(token);
            if (!StringUtils.isEmpty(username)) {
                // 从redis中获取权限数据
                String authString = redisTemplate.opsForValue().get(username);
                if (!StringUtils.isEmpty(authString)) {
                    List<Map> mapList = JSON.parseArray(authString, Map.class);
                    System.out.println(mapList);
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    mapList.forEach(map -> {
                        String authority = (String) map.get("authority");
                        authorities.add(new SimpleGrantedAuthority(authority));
                    });
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                } else {
                    return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                }
            }
        }
        return null;
    }
}
