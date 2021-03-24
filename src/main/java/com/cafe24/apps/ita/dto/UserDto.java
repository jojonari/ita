package com.cafe24.apps.ita.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    private Long idx;
    private String userId;
    private String userName;
    private String teamName;
    private Set<String> operationLevel;
    private Set<String> grantType;

    @Builder
    public UserDto(Long idx, String userId, String userName, String teamName, Set<String> operationLevel, Set<String> grantType) {
        this.idx = idx;
        this.userId = userId;
        this.userName = userName;
        this.teamName = teamName;
        this.operationLevel = operationLevel;
        this.grantType = grantType;
    }
}
