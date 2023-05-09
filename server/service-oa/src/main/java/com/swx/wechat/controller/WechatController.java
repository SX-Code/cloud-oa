package com.swx.wechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.swx.auth.service.SysUserService;
import com.swx.common.annotation.ResponseResult;
import com.swx.common.jwt.JwtHelper;
import com.swx.common.pojo.BizException;
import com.swx.model.system.SysUser;
import com.swx.security.custom.LoginUserInfoHelper;
import com.swx.vo.wechat.BindPhoneVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static java.sql.Types.NULL;

@Api(tags = "微信登陆")
@RestController
@ResponseResult
@RequestMapping("/admin/wechat")
public class WechatController {

    private final SysUserService sysUserService;
    private final WxMpService wxMpService;

    public WechatController(SysUserService sysUserService, WxMpService wxMpService) {
        this.sysUserService = sysUserService;
        this.wxMpService = wxMpService;
    }

    @GetMapping("/authorize")
    public Map<String, String> authorize(@RequestParam("redirect_url") String returnUrl) {
        String redirectURL = wxMpService.getOAuth2Service()
                .buildAuthorizationUrl(URLDecoder.decode(returnUrl),
                        WxConsts.OAuth2Scope.SNSAPI_USERINFO,
                        null);
        HashMap<String, String> map = new HashMap<>();
        map.put("redirectUrl", redirectURL);
        return map;
    }

    @GetMapping("/userInfo")
    public Map<String, String> userInfo(@RequestParam("code") String code) throws WxErrorException {
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
        String openId = accessToken.getOpenId();
        WxOAuth2UserInfo wxMpUser = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);

        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOpenId, openId));
        String token = null;
        if (null != sysUser) {
            // 已经绑定过了
            token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("openId", openId);
        return map;
    }

    @ApiOperation(value = "微信账号绑定手机")
    @PostMapping("bind")
    public Map<String, String> bindPhone(@RequestBody BindPhoneVo bindPhoneVo) {
        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, bindPhoneVo.getPhone()));
        if (null != sysUser) {
            sysUser.setOpenId(bindPhoneVo.getOpenId());
            sysUserService.updateById(sysUser);
            String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
            HashMap<String, String> map = new HashMap<>();
            map.put("token", token);
            return map;
        } else {
            throw new BizException("手机号不存在，绑定失败");
        }
    }

    @ApiOperation(value = "解除微信账号绑定手机")
    @PostMapping("unbind")
    @ResponseResult
    public void unbind() {
        UpdateWrapper<SysUser> set = new UpdateWrapper<SysUser>()
                .eq("id", LoginUserInfoHelper.getUserId())
                .set("open_id", null);
        sysUserService.update(set);
    }
}
