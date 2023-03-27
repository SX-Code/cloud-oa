package com.swx.vo.system;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserQueryVo implements Serializable {

    public static final long serialVersionUID = 1L;

    private String keyword;

    private String createTimeBegin;
    private String createTimeEnd;

    private Long roleId;
    private Long postId;
    private Long deptId;
}
