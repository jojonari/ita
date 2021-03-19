package com.cafe24.apps.ita.dto;

import com.cafe24.apps.ita.entity.User;
import com.cafe24.apps.ita.util.JsonUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * toEntity
     *
     * @return
     */
    public User toEntity() {
        return User.builder()
                .idx(idx)
                .userId(userId)
                .userName(userName)
                .teamName(teamName)
                .operationLevel(operationLevel)
                .grantType(grantType)
                .build();
    }

    /**
     * operationLevelTextValue
     * @return
     */
    public String operationLevelTextValue() {
        List<TextValue> textValues = this.operationLevel.stream().map(x -> new TextValue(x.toUpperCase(), x.toLowerCase())).collect(Collectors.toList());
        return JsonUtil.toJSON(textValues);
    }
    /**
     * operationLevelTextValue
     * @return
     */
    public String grantTypeTextValue() {
        List<TextValue> textValues = this.grantType.stream().map(x -> new TextValue(x.toUpperCase(), x.toLowerCase())).collect(Collectors.toList());
        return JsonUtil.toJSON(textValues);
    }
}
