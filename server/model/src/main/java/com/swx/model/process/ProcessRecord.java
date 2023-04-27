package com.swx.model.process;

import com.baomidou.mybatisplus.annotation.TableName;
import com.swx.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 审批记录
 * </p>
 *
 * @author sw-code
 * @since 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="OaProcessRecord对象", description="审批记录")
@TableName("oa_process_record")
public class ProcessRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "审批流程id")
    private Long processId;

    @ApiModelProperty(value = "审批描述")
    private String description;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "操作用户id")
    private Long operateUserId;

    @ApiModelProperty(value = "操作用户")
    private String operateUser;


}
