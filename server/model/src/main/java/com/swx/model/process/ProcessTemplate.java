package com.swx.model.process;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.swx.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 审批模板
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="OaProcessTemplate对象", description="审批模板")
@TableName("oa_process_template")
public class ProcessTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模板名称")
    private String name;

    @ApiModelProperty(value = "图标路径")
    private String iconUrl;

    private Long processTypeId;

    @ApiModelProperty(value = "表单属性")
    private String formProps;

    @ApiModelProperty(value = "表单选项")
    private String formOptions;

    @ApiModelProperty(value = "流程定义key")
    private String processDefinitionKey;

    @ApiModelProperty(value = "流程定义上传路径")
    private String processDefinitionPath;

    @ApiModelProperty(value = "流程定义模型id")
    private String processModelId;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @TableField(exist = false)
    private String processTypeName;


}
