package com.swx.vo.process;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "流程表单")
public class ProcessFormVo {

    @ApiModelProperty(value = "审批模版ID")
    private Long processTemplateId;

    @ApiModelProperty(value = "审批类型ID")
    private Long processTypeId;

    @ApiModelProperty(value = "表单值")
    private String formValues;

}
