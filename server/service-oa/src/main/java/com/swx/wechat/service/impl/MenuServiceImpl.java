package com.swx.wechat.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.swx.model.wechat.Menu;
import com.swx.wechat.mapper.MenuMapper;
import com.swx.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-05-03
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final WxMpService wxMpService;

    @Value("${wechat.prefix}")
    private String prefix;

    public MenuServiceImpl(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    /**
     * 获取所有菜单
     */
    @Override
    public List<Menu> listMenu() {
        List<Menu> menuList = baseMapper.selectList(null);
        List<Menu> parents = menuList.stream()
                .filter(menu -> menu.getParentId() == 0)
                .collect(Collectors.toList());
        ArrayList<Menu> menus = new ArrayList<>();
        for (Menu menu : parents) {
            List<Menu> children = menuList.stream()
                    .filter(item -> Objects.equals(item.getParentId(), menu.getId()))
                    .collect(Collectors.toList());
            menu.setChildren(children);
            menus.add(menu);
        }
        return menus;
    }

    /**
     * 同步菜单到微信公众号
     */
    @Override
    public void syncMenu() throws WxErrorException {
        // 查询菜单数据，封装成微信官方格式
        List<Menu> menuList = this.listMenu();
        // 菜单
        JSONArray buttonList = new JSONArray();
        for (Menu menu : menuList) {
            JSONObject button = new JSONObject();
            button.put("name", menu.getName());
            if (CollectionUtils.isEmpty(menu.getChildren())) {
                button.put("type", menu.getType());
                button.put("url", prefix + "/oa/#" + menu.getUrl());
            } else {
                JSONArray subButton = new JSONArray();
                for (Menu child : menu.getChildren()) {
                    JSONObject view = new JSONObject();
                    String type = child.getType();
                    view.put("type", type);
                    view.put("name", child.getName());
                    if (type.equals("click")) {
                        // 点击类型
                        view.put("key", child.getMeunKey());
                    } else {
                        // 网页或者小程序类型
                        view.put("url", prefix + "/oa/#" + child.getUrl());
                    }
                    subButton.add(view);
                }
                button.put("sub_button", subButton);
            }
            buttonList.add(button);
        }
        // 菜单对象
        JSONObject button = new JSONObject();
        button.put("button", buttonList);

        // 推送
        wxMpService.getMenuService().menuCreate(button.toString());
    }

    /**
     * 删除微信公众号菜单
     */
    @Override
    public void removeMenu() throws WxErrorException {
        wxMpService.getMenuService().menuDelete();
    }
}
