package com.swx.model.process;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.swx.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="OaProcessType对象", description="")
@TableName("oa_process_type")
public class ProcessType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "审批类型名称")
    private String name;

    @ApiModelProperty(value = "审批描述")
    private String description;

    @TableField(exist = false)
    private List<ProcessTemplate> processTemplateList;


}
