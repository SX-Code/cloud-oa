package com.swx.process.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.auth.service.SysUserService;
import com.swx.model.process.Process;
import com.swx.model.process.ProcessRecord;
import com.swx.model.process.ProcessTemplate;
import com.swx.model.system.SysUser;
import com.swx.process.mapper.ProcessMapper;
import com.swx.process.service.ProcessRecordService;
import com.swx.process.service.ProcessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swx.process.service.ProcessTemplateService;
import com.swx.security.custom.LoginUserInfoHelper;
import com.swx.vo.process.ApprovalVo;
import com.swx.vo.process.ProcessFormVo;
import com.swx.vo.process.ProcessQueryVo;
import com.swx.vo.process.ProcessVo;
import com.swx.vo.system.page.CustomPage;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {

    private final RepositoryService repositoryService;
    private final SysUserService sysUserService;

    @Autowired
    private ProcessTemplateService processTemplateService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final ProcessRecordService processRecordService;
    private final HistoryService historyService;

    public ProcessServiceImpl(RepositoryService repositoryService, SysUserService sysUserService, RuntimeService runtimeService, TaskService taskService, ProcessRecordService processRecordService, HistoryService historyService) {
        this.repositoryService = repositoryService;
        this.sysUserService = sysUserService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.processRecordService = processRecordService;
        this.historyService = historyService;
    }

    /**
     * 分页查询审批流程
     *
     * @param pageParam      分页参数
     * @param processQueryVo 查询参数
     * @return 多表查询的审批流程
     */
    @Override
    public IPage<Process> selectPageVo(CustomPage<ProcessVo> pageParam, ProcessQueryVo processQueryVo) {
        return baseMapper.selectPageVo(pageParam, processQueryVo);
    }

    /**
     * 启动流程实例
     */
    @Override
    public void startUp(ProcessFormVo processFormVo) {
        // 获取用户信息
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
        // 根据审批模版id，查询模板信息
        LambdaQueryWrapper<ProcessTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ProcessTemplate::getId, ProcessTemplate::getName, ProcessTemplate::getProcessDefinitionKey)
                .eq(ProcessTemplate::getId, processFormVo.getProcessTemplateId());
        ProcessTemplate processTemplate = processTemplateService.getOne(wrapper);
        // 保存流程信息
        Process process = new Process();
        BeanUtils.copyProperties(processFormVo, process);
        process.setStatus(1);
        String workNo = String.valueOf(System.currentTimeMillis());
        process.setProcessCode(workNo);
        process.setUserId(LoginUserInfoHelper.getUserId());
        process.setFormValues(processFormVo.getFormValues());
        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");
        baseMapper.insert(process);
        // 启动流程实例
        // 流程定义key
        String key = processTemplate.getProcessDefinitionKey();
        // 业务流程id
        String businessId = String.valueOf(process.getId());
        // 流程参数
        HashMap<String, Object> variables = new HashMap<>();
        JSONObject jsonObject = JSON.parseObject(processFormVo.getFormValues());
        JSONObject formData = jsonObject.getJSONObject("formData");
        HashMap<String, Object> map = new HashMap<>(formData);
        variables.put("data", map);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, businessId, variables);
        // 查询下一个审批人
        List<Task> taskList = this.getCurrentTaskList(processInstance.getId());
        String names = taskList.stream().map(task -> {
            String assignee = task.getAssignee();
            SysUser user = sysUserService.getUserByUsername(assignee);
            // TODO 推送消息
            return user.getName();
        }).collect(Collectors.joining(","));
        // 业务流程关联
        process.setProcessInstanceId(processInstance.getId());
        process.setDescription("等待" + names + "审批");
        baseMapper.updateById(process);
        processRecordService.record(process.getId(), 1, "发起申请");
    }

    /**
     * 查询待办任务
     *
     * @param pageParam 分页信息
     */
    @Override
    public IPage<ProcessVo> pagePending(Page<Process> pageParam) {
        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .orderByTaskCreateTime()
                .desc();
        int begin = (int) ((pageParam.getCurrent() - 1) * pageParam.getSize());
        int size = (int) pageParam.getSize();
        List<Task> taskList = query.listPage(begin, size);
        List<ProcessVo> processVoList = new ArrayList<>();
        for (Task task : taskList) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            Process process = baseMapper.selectById(Long.parseLong(businessKey));
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setTaskId(task.getId());
            processVoList.add(processVo);
        }
        IPage<ProcessVo> page = new Page<>(pageParam.getCurrent(), pageParam.getSize(), query.count());
        page.setRecords(processVoList);
        return page;
    }

    /**
     * 查询已处理审批
     *
     * @param pageParam 分页参数
     */
    @Override
    public IPage<ProcessVo> pageProcessed(Page<Process> pageParam) {
        // 封装查询信息
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(LoginUserInfoHelper.getUsername())
                .finished()
                .orderByTaskCreateTime().desc();
        // 分页查询已完成的任务
        int begin = (int) ((pageParam.getCurrent() - 1) * pageParam.getSize());
        int size = (int) pageParam.getSize();
        List<HistoricTaskInstance> list = query.listPage(begin, size);
        List<ProcessVo> processVoList = new ArrayList<>();
        for (HistoricTaskInstance item : list) {
            // 根据流程实例ID获取流程信息
            LambdaQueryWrapper<Process> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Process::getProcessInstanceId, item.getProcessInstanceId());
            Process process = baseMapper.selectOne(wrapper);
            // 转换
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setTaskId(item.getId());
            processVoList.add(processVo);
        }
        IPage<ProcessVo> page = new Page<>(pageParam.getCurrent(), pageParam.getSize(), query.count());
        page.setRecords(processVoList);
        return page;
    }

    /**
     * 查询已发起
     *
     * @param pageParam 分页参数
     */
    @Override
    public IPage<Process> pageStarted(Page<Process> pageParam) {
        LambdaQueryWrapper<Process> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Process::getUserId, LoginUserInfoHelper.getUserId());
        return baseMapper.selectPage(pageParam, wrapper);
    }

    /**
     * 查看审批详情信息
     *
     * @param id 审批ID
     */
    @Override
    public Map<String, Object> show(Long id) {
        // 获取审批信息
        Process process = baseMapper.selectById(id);
        if (process == null) return null;
        // 获取审批记录信息
        LambdaQueryWrapper<ProcessRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRecord::getProcessId, id);
        List<ProcessRecord> processRecords = processRecordService.list(wrapper);
        // 获取审批模版
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());
        // 是否可以审批
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        boolean isApprove = false;
        for (Task task : taskList) {
            String username = LoginUserInfoHelper.getUsername();
            if (task.getAssignee().equals(username)) {
                isApprove = true;
                break;
            }
        }
        // 封装map数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("process", process);
        map.put("processRecordList", processRecords);
        map.put("processTemplate", processTemplate);
        map.put("isApprove", isApprove);
        return map;
    }

    /**
     * 审批
     */
    @Override
    public void approve(ApprovalVo approvalVo) {
        // 获取流程变量
        String taskId = approvalVo.getTaskId();
        Map<String, Object> variables = taskService.getVariables(taskId);
        if (approvalVo.getStatus() == 1) {
            // 审批通过
            taskService.complete(taskId);
        } else {
            // 驳回
            this.endTask(taskId);
        }
        // 记录审批过程信息
        String description = approvalVo.getStatus() == 1 ? "已通过" : "已拒绝";
        processRecordService.record(approvalVo.getProcessId(), approvalVo.getStatus(), description);
        // 查询下一个审批人
        Process process = baseMapper.selectById(approvalVo.getProcessId());
        List<Task> taskList = this.getCurrentTaskList(process.getProcessInstanceId());
        if (!CollectionUtils.isEmpty(taskList)) {
            String names = taskList.stream().map(task -> {
                String assignee = task.getAssignee();
                SysUser user = sysUserService.getUserByUsername(assignee);
                // TODO 推送消息
                return user.getName();
            }).collect(Collectors.joining(","));
            process.setDescription("等待" + names + "审批");
            process.setStatus(1);
        } else {
            if (approvalVo.getStatus() == 1) {
                process.setDescription("审批完成（通过）");
                process.setStatus(2);
            } else {
                process.setDescription("审批完成（通过）");
                process.setStatus(-1);
            }
        }
        baseMapper.updateById(process);
    }

    // 结束流程
    private void endTask(String taskId) {
        // 获取任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        // 获取流程定义模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        // 获取结束流程节点
        List<EndEvent> endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        if (CollectionUtils.isEmpty(endEventList)) {
            return;
        }
        FlowNode endFlowNode = (FlowNode) endEventList.get(0);
        // 当前流向节点
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        // 临时保留当前活动的原始流向
        // List<SequenceFlow> originalSequenceFlowList = new ArrayList<>(currentFlowNode.getOutgoingFlows());
        // 清理当前流向节点
        currentFlowNode.getOutgoingFlows().clear();

        // 创建新流向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlow");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);

        // 当前节点指向新方向
        ArrayList<SequenceFlow> newSequenceFlows = new ArrayList<>();
        newSequenceFlows.add(newSequenceFlow);
        currentFlowNode.setOutgoingFlows(newSequenceFlows);

        taskService.complete(taskId);
    }

    // 获取当前任务的列表
    private List<Task> getCurrentTaskList(String id) {
        return taskService.createTaskQuery().processInstanceId(id).list();
    }
}
