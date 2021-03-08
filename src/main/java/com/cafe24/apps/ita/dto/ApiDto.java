package com.cafe24.apps.ita.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiDto {
    private String mallId;
    private String clientId;
    private String methods;
    private String version;
    private String url;
    private String requestBody;
    private String responseBody;
}