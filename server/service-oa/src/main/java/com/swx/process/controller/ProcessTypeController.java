package com.swx.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.common.annotation.ResponseResult;
import com.swx.common.pojo.BizException;
import com.swx.model.process.ProcessType;
import com.swx.process.service.ProcessTypeService;
import com.swx.vo.system.page.CustomPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 *  审批类型前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
@Api(value = "审批类型", tags = "审批类型")
@RestController
@CrossOrigin
@ResponseResult
@RequestMapping("/admin/process/processType")
public class ProcessTypeController {

    private final ProcessTypeService processTypeService;

    public ProcessTypeController(ProcessTypeService processTypeService) {
        this.processTypeService = processTypeService;
    }

    @PreAuthorize("hasAuthority('process_type_list')")
    @ApiOperation(value = "获取所有审批分类列表")
    @GetMapping("")
    public List<ProcessType> list() {
        return processTypeService.list();
    }

    @PreAuthorize("hasAuthority('process_type_list')")
    @ApiOperation(value = "分页获取审批类型列表")
    @GetMapping("{page}/{limit}")
    public IPage<ProcessType> page(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit
    ) {
        CustomPage<ProcessType> pageParam = new CustomPage(page, limit);
        return processTypeService.page(pageParam);
    }

    @PreAuthorize("hasAuthority('process_type_list')")
    @ApiOperation("获取审批类型")
    @GetMapping("{id}")
    public ProcessType get(@PathVariable Long id) {
        return processTypeService.getById(id);
    }

    @PreAuthorize("hasAuthority('process_type_add')")
    @ApiOperation("新增审批类型")
    @PostMapping("")
    public void add(@RequestBody ProcessType processType) {
        boolean save = processTypeService.save(processType);
        if (!save) {
            throw new BizException("审批类型新增失败");
        }
    }

    @PreAuthorize("hasAuthority('process_type_update')")
    @ApiOperation("修改审批类型")
    @PutMapping("")
    public void update(@RequestBody ProcessType processType) {
        boolean update = processTypeService.updateById(processType);
        if (!update) {
            throw new BizException("审批类型修改失败");
        }
    }

    @PreAuthorize("hasAuthority('process_type_remove')")
    @ApiOperation("删除审批类型")
    @DeleteMapping("{id}")
    public void remove(@PathVariable Long id) {
        boolean remove = processTypeService.removeById(id);
        if (!remove) {
            throw new BizException("审批类型删除失败");
        }
    }
}

