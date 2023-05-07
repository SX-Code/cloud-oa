package com.swx.vo.process;

import lombok.Data;

@Data
public class ApprovalVo {

    private Long processId;
    private String taskId;
    private Integer status;
    private String description;
}
