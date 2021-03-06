package com.cafe24.apps.ita.dto;

import com.cafe24.apps.ita.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
public class UserDto {

    private Long idx;
    private String userId;
    private String userName;
    private String teamName;

    @Builder
    public UserDto(Long idx, String userId, String userName, String teamName) {
        this.idx = idx;
        this.userId = userId;
        this.userName = userName;
        this.teamName = teamName;
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
                .build();
    }
}
