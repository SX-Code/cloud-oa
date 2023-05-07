package com.swx.process.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.swx.common.annotation.ResponseResult;
import com.swx.model.process.Process;
import com.swx.process.service.ProcessService;
import com.swx.vo.process.ProcessQueryVo;
import com.swx.vo.process.ProcessVo;
import com.swx.vo.system.page.CustomPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 审批流程 前端控制器
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
@RestController
@ResponseResult
@RequestMapping("/admin/process")
public class ProcessController {

    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @ApiOperation("分页查询审批流程")
    @GetMapping("{page}/{limit}")
    public IPage<Process> page(@PathVariable Integer page,
                               @PathVariable Integer limit,
                               ProcessQueryVo processQueryVo) {
        CustomPage<ProcessVo> pageParam = new CustomPage<>(page, limit);
        return processService.selectPageVo(pageParam, processQueryVo);
    }
}

