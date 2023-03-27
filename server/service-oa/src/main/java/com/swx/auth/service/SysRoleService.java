package com.swx.auth.service;

import com.swx.model.system.AssignRoleVo;
import com.swx.model.system.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-03-03
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 获取所有角色信息，和用户所属角色信息
     * @param userId 用户id
     */
    Map<String, Object> findRoleDataByUserId(Long userId);

    void doAssign(AssignRoleVo assignRoleVo);
}
