package com.cafe24.apps.ita.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
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

    /**
     * HMAC hashing
     *
     * @param plainText
     * @param secretKey
     * @return
     */
    public static String makeHmac(String plainText, String secretKey) {
        String CypHmac = "";

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(), "HmacSHA256"));
            mac.update(plainText.getBytes(StandardCharsets.UTF_8));

            CypHmac = Base64.encodeBase64String(mac.doFinal());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return CypHmac;
    }
}
