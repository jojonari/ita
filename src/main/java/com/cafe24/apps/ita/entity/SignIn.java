package com.cafe24.apps.ita.entity;

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
     * @param user
     * @return
     * @throws NoSuchAlgorithmException
     */
    public boolean isEqualsPassWord(User user) throws NoSuchAlgorithmException {
        String encUserPw = EncryptUtil.encryptPassword(this.userPw);
        return encUserPw.equals(user.getUserPw());
    }
}
