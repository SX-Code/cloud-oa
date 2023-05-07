package com.swx.vo.process;

import com.swx.model.process.Process;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "流程VO")
public class ProcessVo extends Process {

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "审批模板名称")
    private String processTemplateName;

    @ApiModelProperty(value = "审批类型名称")
    private String processTypeName;

    @ApiModelProperty(value = "表单属性")
    private String formProps;

    @ApiModelProperty(value = "表单选项")
    private String formOptions;

    @ApiModelProperty(value = "任务ID")
    private String taskId;
}
