package com.swx.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swx.model.process.Process;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swx.model.process.ProcessType;
import com.swx.vo.process.ApprovalVo;
import com.swx.vo.process.ProcessFormVo;
import com.swx.vo.process.ProcessQueryVo;
import com.swx.vo.process.ProcessVo;
import com.swx.vo.system.page.CustomPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
public interface ProcessService extends IService<Process> {

    /**
     * 分页查询审批流程
     * @param pageParam      分页参数
     * @param processQueryVo 查询参数
     * @return 多表查询的审批流程
     */
    IPage<Process> selectPageVo(CustomPage<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    /**
     * 启动流程实例
     */
    void startUp(ProcessFormVo processFormVo);

    /**
     * 查询待办任务
     * @param pageParam 分页信息
     */
    IPage<ProcessVo> pagePending(Page<Process> pageParam);

    /**
     * 查看审批详情信息
     * @param id 审批ID
     */
    Map<String, Object> show(Long id);

    /**
     * 审批
     */
    void approve(ApprovalVo approvalVo);

    /**
     * 查询已处理审批
     * @param pageParam 分页参数
     */
    IPage<ProcessVo> pageProcessed(Page<Process> pageParam);

    /**
     * 查询已发起
     * @param pageParam 分页参数
     */
    IPage<Process> pageStarted(Page<Process> pageParam);
}
