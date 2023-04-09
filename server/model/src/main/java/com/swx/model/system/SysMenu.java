package com.swx.model.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.swx.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author sw-code
 * @since 2023-03-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysMenu对象", description="菜单表")
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属上级")
    private Long parentId;

    @ApiModelProperty(value = "标题名称")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subtitle;

    @ApiModelProperty(value = "类型(0:目录,1:菜单,2:按钮)")
    private Integer type;

    @ApiModelProperty(value = "路由地址")
    private String path;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "权限标识")
    private String auth;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序")
    private Integer sortValue;

    @ApiModelProperty(value = "状态(0:禁止,1:正常)")
    private Integer status;

    // 下级列表
    @TableField(exist = false)
    private List<SysMenu> children;
    // 是否选中
    @TableField(exist = false)
    private Boolean isSelect;


}
