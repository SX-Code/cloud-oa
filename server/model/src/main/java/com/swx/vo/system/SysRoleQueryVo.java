package com.swx.vo.system;

import java.io.Serializable;

/**
 * @author sw-code
 * @since 2023-03-03
 */
public class SysRoleQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
