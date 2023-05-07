package com.swx.security.config;

import com.swx.security.custom.CustomMd5PasswordEncoder;
import com.swx.security.filter.TokenAuthenticationFilter;
import com.swx.security.filter.TokenLoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;

    private final CustomMd5PasswordEncoder customMd5PasswordEncoder;

    private final RedisTemplate<String, String> redisTemplate;

    public WebSecurityConfig(UserDetailsService userDetailsService, CustomMd5PasswordEncoder customMd5PasswordEncoder, RedisTemplate<String, String> redisTemplate) {
        this.userDetailsService = userDetailsService;
        this.customMd5PasswordEncoder = customMd5PasswordEncoder;
        this.redisTemplate = redisTemplate;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/admin/system/index/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new TokenAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new TokenLoginFilter(authenticationManager(), redisTemplate));
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(customMd5PasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
           .antMatchers(
                   "/favicon.icon", "/swagger-resources/**", "/webjars/**",
                   "/v2/**", "/swagger-ui.html/**", "/doc.html",
                   "/admin/wechat/authorize", "/admin/wechat/userInfo", "/admin/wechat/bind");
    }
}
