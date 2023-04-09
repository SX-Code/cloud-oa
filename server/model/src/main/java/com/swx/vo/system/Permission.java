package com.swx.vo.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Permission {

    @JsonProperty("label")
    private String title;

    @JsonProperty("value")
    private String auth;
}
