package com.cafe24.apps.ita.dto;

import com.cafe24.apps.ita.util.EncryptUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.security.NoSuchAlgorithmException;

@Data
public class SignIn {
    @NotBlank
    private String userId;
    @NotBlank
    private String userPw;
    private String callbackUrl;

    /**
     * 패스워드 비교
     *
     * @param privateUserDto
     * @return
     * @throws NoSuchAlgorithmException
     */
    public boolean isEqualsPassWord(PrivateUserDto privateUserDto) throws NoSuchAlgorithmException {
        String encUserPw = EncryptUtil.encryptPassword(this.userPw);
        return privateUserDto.isEqualsPassWord(encUserPw);
    }
}
