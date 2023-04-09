package com.swx.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.swx.auth.mapper.SysUserRoleMapper;
import com.swx.vo.system.AssignRoleVo;
import com.swx.model.system.SysRole;
import com.swx.auth.mapper.SysRoleMapper;
import com.swx.auth.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.model.system.SysUserRole;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-03-03
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 获取所有角色信息，和用户所属角色信息
     * @param userId 用户id
     */
    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {
        // 获取所有角色信息
        List<SysRole> allRoleList = baseMapper.selectList(null);

        // 根据用户id，查询用户角色表
        List<SysUserRole> existUserRoleList =
                sysUserRoleMapper.selectList(
                        new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));

        // 获取角色ids
        List<Long> existUserRoleIdList =
                existUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());
        // 根据角色ids获取角色完整信息
        Stream<SysRole> assignRoleList = allRoleList.stream().filter(c -> existUserRoleIdList.contains(c.getId()));

        // 封装返回结果
        HashMap<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList", assignRoleList);
        roleMap.put("allRoles", allRoleList);
        return roleMap;
    }

    @Override
    public void doAssign(AssignRoleVo assignRoleVo) {
        // 删除分配信息
        sysUserRoleMapper.delete(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, assignRoleVo.getUserId()));
        // 重新分配信息
        for (Long roleId : assignRoleVo.getRoleIdList()) {
            if (!StringUtils.isEmpty(assignRoleVo)) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(assignRoleVo.getUserId());
                sysUserRole.setRoleId(roleId);
                sysUserRoleMapper.insert(sysUserRole);
            }
        }
    }
}
