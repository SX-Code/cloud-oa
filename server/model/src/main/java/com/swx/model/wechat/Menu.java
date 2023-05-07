package com.swx.model.wechat;

import com.baomidou.mybatisplus.annotation.*;
import com.swx.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author sw-code
 * @since 2023-05-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wechat_menu")
@ApiModel(value="Menu对象", description="菜单")
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "网页 链接，用户点击菜单可打开链接")
    private String url;

    @ApiModelProperty(value = "菜单KEY值，用于消息接口推送")
    private String meunKey;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    // 下级列表
    @TableField(exist = false)
    private List<Menu> children;

}
