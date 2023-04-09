package com.swx.auth.mapper;

import com.swx.vo.system.MenuWithIdType;
import com.swx.vo.system.Permission;
import com.swx.model.system.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author sw-code
 * @since 2023-03-29
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<Permission> queryAuthListByUserId(@Param("userId") Long userId);

    List<MenuWithIdType> selectIdTypeList(@Param("roleId") Long roleId);
}
