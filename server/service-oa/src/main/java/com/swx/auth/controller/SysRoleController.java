package com.swx.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.auth.service.SysRoleService;
import com.swx.common.annotation.ResponseResult;
import com.swx.common.pojo.BizException;
import com.swx.model.system.SysRole;
import com.swx.vo.system.SysRoleQueryVo;
import com.swx.vo.system.page.CustomPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-03-03
 */
@Api(tags = "角色管理接口")
@RestController
@ResponseResult
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public List<SysRole> findAll() {
        return sysRoleService.list();
    }

    /**
     *
     * @param page  当前页
     * @param limit 记录数
     * @param sysRoleQueryVo 查询参数
     * @return 分页信息
     */
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public IPage<SysRole> pageQueryRole(@PathVariable Long page,
                                       @PathVariable Long limit,
                                       SysRoleQueryVo sysRoleQueryVo) {
        // 自定义Page，修改current为page，和前端保持一致
        CustomPage<SysRole> pageParam = new CustomPage<>(page, limit);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)) {
            wrapper.like(SysRole::getRoleName, roleName);
        }
        IPage<SysRole> iPage = sysRoleService.page(pageParam, wrapper);
        return iPage;
    }

    @ApiOperation("添加角色")
    @PostMapping("")
    public void save(@RequestBody SysRole role) {
        boolean save = sysRoleService.save(role);
        if (!save) {
            throw new BizException("添加失败");
        }
    }

    /**
     * 根据id查询角色
     * @param id 角色id
     * @return 角色
     */
    @ApiOperation("根据ID查询")
    @GetMapping("{id}")
    public SysRole get(@PathVariable Long id) {
        return sysRoleService.getById(id);
    }

    /**
     * 更新角色
     * @param role 角色信息
     */
    @ApiOperation("更新角色")
    @PutMapping("")
    public void get(@RequestBody SysRole role) {
        boolean update = sysRoleService.updateById(role);
        if (!update) {
            throw new BizException("更新失败");
        }
    }

    @ApiOperation("根据id删除")
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        boolean delete = sysRoleService.removeById(id);
        if (!delete) {
            throw new BizException("删除失败");
        }
    }

    @ApiOperation("批量删除")
    @DeleteMapping("batch")
    public void batchRemove(@RequestBody List<Long> ids) {
        boolean delete = sysRoleService.removeByIds(ids);
        if (!delete) {
            throw new BizException("删除失败");
        }
    }

}

