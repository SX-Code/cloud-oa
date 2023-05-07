package com.swx.vo.process;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "流程查询VO")
public class ProcessQueryVo {

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty("用户ID")
    private Long userId;

    @TableField("process_template_id")
    private Long processTemplateId;

    @ApiModelProperty("审批类型ID")
    private Long processTypeId;

    @ApiModelProperty("开始时间")
    private String createTimeBegin;

    @ApiModelProperty("结束时间")
    private String createTimeEnd;

    @ApiModelProperty("状态（0: 默认 1: 审批中 2: 审批通过 -1: 驳回）")
    private Integer status;
}

