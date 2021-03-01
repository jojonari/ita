package com.cafe24.apps.ita.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebhookDto {

    private Long idx;
    private String clientId;
    private double eventNo;
    private String xTraceId;
    private String resource;
    private String createdDate;
    private String modifiedDate;

}
