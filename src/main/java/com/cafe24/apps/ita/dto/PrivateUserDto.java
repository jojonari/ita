package com.cafe24.apps.ita.dto;

import com.cafe24.apps.ita.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
public class PrivateUserDto {

    private Long idx;
    private String userId;
    private String userPw;
    private String userName;
    private String teamName;
    private Set<String> operationLevel;
    private Set<String> grantType;

    @Builder
    public PrivateUserDto(Long idx, String userId, String userPw, String userName, String teamName, Set<String> operationLevel, Set<String> grantType) {
        this.idx = idx;
        this.userId = userId;
        this.userPw = userPw;
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
                .userPw(userPw)
                .userName(userName)
                .teamName(teamName)
                .operationLevel(operationLevel)
                .grantType(grantType)
                .build();
    }

    /**
     * 패스워드 같은지 비교
     * @param encUserPw
     * @return
     */
    public boolean isEqualsPassWord(String encUserPw) {
        return this.userPw.equals(encUserPw);
    }
}
