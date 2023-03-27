package com.swx.auth.service.impl;

import com.swx.model.system.SysUser;
import com.swx.auth.mapper.SysUserMapper;
import com.swx.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-03-24
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public boolean updateStatus(Long id, Integer status) {
        // 根据id查询
        SysUser sysUser = baseMapper.selectById(id);
        sysUser.setStatus(status);
        return baseMapper.updateById(sysUser) > 0;
    }
}
