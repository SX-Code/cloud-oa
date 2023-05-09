package com.swx.wechat.service;

import com.swx.model.process.Process;
import com.swx.model.system.SysUser;

public interface MessageService {

    /**
     * 推送待审批人员
     * @param process 流程ID
     * @param user 用户ID
     * @param taskId 任务ID
     */
    void pushPendingMessage(Process process, SysUser user, String taskId);

    /**
     * 审批后推送提交审批人员
     * @param process 流程ID
     * @param status 任务ID
     */
    void pushProcessedMessage(Process process, Integer status);
}
