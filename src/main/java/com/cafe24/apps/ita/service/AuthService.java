package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.repository.AppRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class AuthService {
    private final AppRepository appRepository;

    public AuthService(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    /**
     * hmac 검증
     * @param appId
     * @param queryString
     * @return
     * @throws Exception
     */
    public boolean checkHmac(Long appId, String queryString) throws Exception {
        Optional<App> app = appRepository.findById(appId);
        app.orElseThrow(() -> new Exception("app 정보가 없습니다."));

        String[] queryArr = queryString.split("&hmac=");
        String requestHmac = URLDecoder.decode(queryArr[1], StandardCharsets.UTF_8);
        String hmac = this.makeHmac(queryArr[0], app.get().getSecretKey());

        return requestHmac.equals(hmac);
    }


    /**
     * HMAC hashing
     *
     * @param plainText
     * @param secretKey
     * @return
     */
    private String makeHmac(String plainText, String secretKey) {
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
