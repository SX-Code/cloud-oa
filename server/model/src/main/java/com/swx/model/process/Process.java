package com.swx.model.process;

import com.baomidou.mybatisplus.annotation.TableName;
import com.swx.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 审批类型
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="OaProcess对象", description="审批类型")
@TableName("oa_process")
public class Process extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "审批code")
    private String processCode;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "审批模板id")
    private Long processTemplateId;

    @ApiModelProperty(value = "审批类型id")
    private Long processTypeId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "表单值")
    private String formValues;

    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;

    @ApiModelProperty(value = "当前审批人")
    private String currentAuditor;

    @ApiModelProperty(value = "状态（0：默认 1：审批中 2：审批通过 -1：驳回）")
    private Integer status;


}
