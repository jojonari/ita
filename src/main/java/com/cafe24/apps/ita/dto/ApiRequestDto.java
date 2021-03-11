package com.cafe24.apps.ita.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiRequestDto {
    private Long idx;
    private String mallId;
    private String clientId;
    private String method;
    private String version;
    private String path;
    private String requestBody;
    private String response;
    private String createdDate;
    private String modifiedDate;
}