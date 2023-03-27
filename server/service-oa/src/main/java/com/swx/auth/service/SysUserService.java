package com.swx.auth.service;

import com.swx.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-03-24
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 状态更新
     * @param id 用户id
     * @param status 状态
     * @return 是否成功
     */
    boolean updateStatus(Long id, Integer status);
}
