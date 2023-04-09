package com.swx.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.swx.auth.service.SysUserService;
import com.swx.common.annotation.ResponseResult;
import com.swx.common.pojo.BizException;
import com.swx.common.pojo.ResultCode;
import com.swx.common.utils.MD5;
import com.swx.model.system.SysUser;
import com.swx.vo.system.SysUserQueryVo;
import com.swx.vo.system.page.CustomPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-03-24
 */
@Api(tags = "用户管理接口")
@RestController
@ResponseResult
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @ApiOperation("更新状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Integer updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        if (status != 0 && status != 1) {
            throw new BizException(ResultCode.PARAM_IS_INVALID);
        }
        if (!sysUserService.updateStatus(id, status)) {
            throw new BizException("状态更新失败");
        }
        return status;
    }

    @ApiOperation("用户条件分页查询")
    @GetMapping("{page}/{limit}")
    public IPage<SysUser> index(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysUserQueryVo sysUserQueryVo) {
        CustomPage<SysUser> pageParam = new CustomPage<>(page, limit);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        // 获取条件值
        String keyword = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        if (!StringUtils.isEmpty(keyword)) {
            wrapper.like(SysUser::getUsername, keyword).or()
                    .like(SysUser::getName, keyword).or()
                    .like(SysUser::getPhone, keyword);
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            ;
            wrapper.apply("UNIX_TIMESTAMP(create_time) >= " + Long.parseLong(createTimeBegin) / 1000);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.apply("UNIX_TIMESTAMP(create_time) < " + Long.parseLong(createTimeEnd) / 1000);
        }

        IPage<SysUser> pageModel = sysUserService.page(pageParam, wrapper);
        return pageModel;
    }

    @ApiOperation(value = "获取用户")
    @GetMapping("{id}")
    public SysUser get(@PathVariable Long id) {
        return sysUserService.getById(id);
    }

    @ApiOperation(value = "保存用户")
    @PostMapping("")
    public void save(@RequestBody SysUser user) {
        // 使用md5对密码进行加密
        String passwordMD5 = MD5.encrypt(user.getPassword());
        user.setPassword(passwordMD5);
        boolean save = sysUserService.save(user);
        if (!save) {
            throw new BizException("添加失败");
        }
    }

    @ApiOperation(value = "更新用户")
    @PutMapping("")
    public void update(@RequestBody SysUser user) {
        boolean update = sysUserService.updateById(user);
        if (!update) {
            throw new BizException("更新失败");
        }
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("{id}")
    public void remove(@PathVariable Long id) {
        boolean remove = sysUserService.removeById(id);
        if (!remove) {
            throw new BizException("删除失败");
        }
    }
}

