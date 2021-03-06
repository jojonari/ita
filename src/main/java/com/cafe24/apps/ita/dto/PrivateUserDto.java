package com.cafe24.apps.ita.dto;

import com.cafe24.apps.ita.entity.User;
import com.cafe24.apps.ita.util.EncryptUtil;
import lombok.Builder;
import lombok.Data;

import java.security.NoSuchAlgorithmException;

@Data
public class PrivateUserDto {

    private Long idx;
    private String userId;
    private String userPw;
    private String userName;
    private String teamName;

    @Builder
    public PrivateUserDto(Long idx, String userId, String userPw, String userName, String teamName) {
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
                .userPw(userPw)
                .userName(userName)
                .teamName(teamName)
                .build();
    }

    /**
     * 패스워드 암호화
     *
     * @throws NoSuchAlgorithmException
     */
    public void encryptPassword() throws NoSuchAlgorithmException {
        this.userPw = EncryptUtil.encryptPassword(this.userPw);
    }

    public boolean isEqualsPassWord(String encUserPw) {
        return this.userPw.equals(encUserPw);
    }
}
