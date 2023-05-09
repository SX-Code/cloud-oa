package com.swx.wechat.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.swx.auth.service.SysUserService;
import com.swx.model.process.Process;
import com.swx.model.process.ProcessTemplate;
import com.swx.model.system.SysUser;
import com.swx.process.service.ProcessTemplateService;
import com.swx.security.custom.LoginUserInfoHelper;
import com.swx.wechat.service.MessageService;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    private final WxMpService wxMpService;
    private final ProcessTemplateService processTemplateService;
    private final SysUserService sysUserService;

    @Value("${wechat.prefix}")
    private String prefix;

    public MessageServiceImpl(WxMpService wxMpService, ProcessTemplateService processTemplateService, SysUserService sysUserService) {
        this.wxMpService = wxMpService;
        this.processTemplateService = processTemplateService;
        this.sysUserService = sysUserService;
    }

    /**
     * 推送待审批人员
     *
     * @param process 流程ID
     * @param user    用户ID
     * @param taskId  任务ID
     */
    @SneakyThrows
    @Override
    public void pushPendingMessage(Process process, SysUser user, String taskId) {
        // 根据这些ID查询数据
        ProcessTemplate processTemplate = processTemplateService.getOne(
                new LambdaQueryWrapper<ProcessTemplate>()
                        .select(ProcessTemplate::getName)
                        .eq(ProcessTemplate::getId, process.getProcessTemplateId()));
        SysUser submitUser = sysUserService.getOne(
                new LambdaQueryWrapper<SysUser>()
                        .select(SysUser::getName)
                        .eq(SysUser::getId, LoginUserInfoHelper.getUserId()));
        String openId = user.getOpenId();
        if (openId == null) {
            return;
        }
        // 构建模板消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openId) // 消息接受人的openId
                .templateId("PJaBg1zM5JeOsyc8T-2YVrYchQawCTT6etOhrpVequU") // 模板ID
                .url(prefix + "/show/" + process.getId() + "/" + taskId + "/0")
                .build();

        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        JSONObject formShowData = jsonObject.getJSONObject("formShowData");
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, Object> entry : formShowData.entrySet()) {
            content.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        // 设置模板变量值
        templateMessage.addData(new WxMpTemplateData("first", submitUser.getName() + "提交" + processTemplate.getName() + "审批申请, 请注意查看。", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword1", process.getProcessCode(), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword2", new DateTime(process.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"), "#272727"));
        templateMessage.addData(new WxMpTemplateData("content", content.toString(), "#272727"));

        // 发送消息
        wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
    }

    /**
     * 推送给审批发起人
     *
     * @param process 流程ID
     * @param status  任务ID
     */
    @SneakyThrows
    @Override
    public void pushProcessedMessage(Process process, Integer status) {
        ProcessTemplate processTemplate = processTemplateService.getOne(
                new LambdaQueryWrapper<ProcessTemplate>()
                        .select(ProcessTemplate::getName)
                        .eq(ProcessTemplate::getId, process.getProcessTemplateId()));
        SysUser user = sysUserService.getById(process.getUserId());
        SysUser currentUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
        String openId = user.getOpenId();
        if (openId == null) {
            return;
        }
        // 构建模板消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openId) // 消息接受人的openId
                .templateId("0Yz0NNSRe84usgX_Acr4ZUL_8jevJ_J3yJt0cEMZj3U") // 模板ID
                .url(prefix + "/show/" + process.getId() + "/0/0")
                .build();

        JSONObject jsonObject = JSON.parseObject(process.getFormValues());
        JSONObject formShowData = jsonObject.getJSONObject("formShowData");
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, Object> entry : formShowData.entrySet()) {
            content.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        // 设置模板变量值
        templateMessage.addData(new WxMpTemplateData("first", user.getName() + "你发起的" + processTemplate.getName() + "审批申请已经被处理了，请注意查看。", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword1", process.getProcessCode(), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword2", new DateTime(process.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"), "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword3", status == 1 ? "审批通过" : "审批拒绝", status == 1 ? "#009966" : "#FF0033"));
        templateMessage.addData(new WxMpTemplateData("keyword4", currentUser.getName(), "#272727"));
        templateMessage.addData(new WxMpTemplateData("content", content.toString(), "#272727"));

        // 发送消息
        wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
    }
}
