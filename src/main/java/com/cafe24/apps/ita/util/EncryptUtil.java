package com.cafe24.apps.ita.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {
    public static final String ALGORITHM = "SHA-512";

    /**
     * 패스워드 암호화
     *
     * @throws NoSuchAlgorithmException
     */
    public static String encryptPassword(String plainText) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(EncryptUtil.ALGORITHM);
        md.update(plainText.getBytes());

        return String.format("%0128x", new BigInteger(1, md.digest()));
    }
}
