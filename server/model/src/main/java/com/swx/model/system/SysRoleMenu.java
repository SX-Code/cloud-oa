package com.swx.model.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.swx.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色菜单
 * </p>
 *
 * @author sw-code
 * @since 2023-03-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysRoleMenu对象", description="角色菜单")
public class SysRoleMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long roleId;

    private Long menuId;


}
