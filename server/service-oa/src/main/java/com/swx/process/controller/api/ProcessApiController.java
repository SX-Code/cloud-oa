package com.swx.process.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.auth.service.SysUserService;
import com.swx.common.annotation.ResponseResult;
import com.swx.model.process.Process;
import com.swx.model.process.ProcessTemplate;
import com.swx.model.process.ProcessType;
import com.swx.model.system.SysUser;
import com.swx.process.service.ProcessService;
import com.swx.process.service.ProcessTemplateService;
import com.swx.process.service.ProcessTypeService;
import com.swx.security.custom.LoginUserInfoHelper;
import com.swx.vo.process.ApprovalVo;
import com.swx.vo.process.ProcessFormVo;
import com.swx.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "审批流程接口")
@RestController
@ResponseResult
@RequestMapping("/web/process")
public class ProcessApiController {

    private final ProcessTypeService processTypeService;
    private final ProcessTemplateService processTemplateService;
    private final ProcessService processService;
    private final SysUserService sysUserService;

    public ProcessApiController(ProcessTypeService processTypeService, ProcessTemplateService processTemplateService, ProcessService processService, SysUserService sysUserService) {
        this.processTypeService = processTypeService;
        this.processTemplateService = processTemplateService;
        this.processService = processService;
        this.sysUserService = sysUserService;
    }

    @ApiOperation(value = "查询待处理任务")
    @GetMapping("/pending/{page}/{limit}")
    public IPage<ProcessVo> pagePending(@PathVariable Long limit, @PathVariable Long page) {
        Page<Process> pageParam = new Page<>(page, limit);
        return processService.pagePending(pageParam);
    }

    @ApiOperation(value = "查询已处理")
    @GetMapping("/processed/{page}/{limit}")
    public IPage<ProcessVo> pageProcessed(@PathVariable Long limit, @PathVariable Long page) {
        Page<Process> pageParam = new Page<>(page, limit);
        return processService.pageProcessed(pageParam);
    }

    @ApiOperation(value = "查询已发起")
    @GetMapping("/started/{page}/{limit}")
    public IPage<Process> pageStarted(@PathVariable Long limit, @PathVariable Long page) {
        Page<Process> pageParam = new Page<>(page, limit);
        return processService.pageStarted(pageParam);
    }

    @ApiOperation(value = "查看审批信息")
    @GetMapping("show/{id}")
    public Map<String, Object> show(@PathVariable Long id) {
        return processService.show(id);
    }

    @ApiOperation(value = "审批")
    @PostMapping("approve")
    public void approve(@RequestBody ApprovalVo approvalVo) {
        processService.approve(approvalVo);
    }

    @ApiOperation(value = "启动流程")
    @PostMapping("/startUp")
    public void startUp(@RequestBody ProcessFormVo processFormVo) {
        processService.startUp(processFormVo);
    }

    @GetMapping("/template/{id}")
    public ProcessTemplate template(@PathVariable Long id) {
        return processTemplateService.getById(id);
    }

    @GetMapping("/list")
    public List<ProcessType> list() {
        List<ProcessType> processTypes = processTypeService.list();
        List<Long> typeIds = processTypes.stream().map(ProcessType::getId).collect(Collectors.toList());
        LambdaQueryWrapper<ProcessTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ProcessTemplate::getId, ProcessTemplate::getProcessTypeId, ProcessTemplate::getName, ProcessTemplate::getIconUrl)
                .eq(ProcessTemplate::getStatus, 1)
                .in(ProcessTemplate::getProcessTypeId, typeIds);
        List<ProcessTemplate> processTemplates = processTemplateService.list(wrapper);
        Map<Long, List<ProcessTemplate>> templatesMap = processTemplates.stream().collect(Collectors.groupingBy(ProcessTemplate::getProcessTypeId));
        for (ProcessType processType : processTypes) {
            processType.setProcessTemplateList(templatesMap.get(processType.getId()));
        }
        return processTypes;
    }

    @GetMapping("user")
    public SysUser getUser() {
        return sysUserService.getById(LoginUserInfoHelper.getUserId());
    }
}
