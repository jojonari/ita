package com.cafe24.apps.ita.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AppDto {
    private Long idx;
    private String appName;
    private String clientId;
    private String partnerId;
    private String grantType;
    private String manageToken;
    private String operationLevel;
    private Set<String> scopes;
    private String createdDate;
    private String modifiedDate;
}
