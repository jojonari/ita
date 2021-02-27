package com.cafe24.apps.ita.dto;

import lombok.Data;

@Data
public class CodeDto {
    private Long appIdx;
    private String code;
    private String state;
    private String error;
    private String error_description;
    private String trace_id;
}
