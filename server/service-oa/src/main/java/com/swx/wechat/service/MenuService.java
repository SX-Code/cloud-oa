package com.swx.wechat.service;

import com.swx.model.wechat.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.model.wechat.Menu;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-05-03
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取所有菜单
     */
    List<Menu> listMenu();

    /**
     * 同步菜单到微信公众号
     */
    void syncMenu() throws WxErrorException;

    /**
     * 删除微信公众号菜单
     */
    void removeMenu() throws WxErrorException;
}
