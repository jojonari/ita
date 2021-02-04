package com.cafe24.apps.ita.entity;

import com.cafe24.apps.ita.util.EncryptUtil;
import lombok.Data;

import java.security.NoSuchAlgorithmException;

@Data
public class SignIn {
    private String userId;
    private String userPw;

    /**
     * 패스워드 비교
     *
     * @param passWord
     * @return
     * @throws NoSuchAlgorithmException
     */
    public boolean isEqualsPassWord(String passWord) throws NoSuchAlgorithmException {
        return passWord.equals(EncryptUtil.encryptPassword(this.userPw));
    }
}
