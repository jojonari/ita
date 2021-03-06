package com.cafe24.apps.ita.dto;

import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.TimeEntity;
import com.cafe24.apps.ita.entity.User;
import com.cafe24.apps.ita.entity.Webhook;
import com.cafe24.apps.ita.util.JsonUtil;
import com.cafe24.apps.ita.util.SessionUtil;
import lombok.*;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Set;


@Data
@Builder
public class PrivateAppDto {

    private Long idx;
    private User user;
    private String appName;
    private String clientId;
    private String secretKey;
    private String partnerId;
    private String grantType;
    private String manageToken;
    private String operationLevel;
    private Set<String> scopes;

    public void setUser(HttpSession session) {
        this.user = SessionUtil.getUserInfo(session).toEntity();
    }

    /**
     * toEntity
     *
     * @return
     */
    public App toEntity() {
        return App.builder()
                .idx(idx)
                .user(user)
                .appName(appName)
                .clientId(clientId)
                .secretKey(secretKey)
                .partnerId(partnerId)
                .grantType(grantType)
                .manageToken(manageToken)
                .operationLevel(operationLevel)
                .scopes(scopes)
                .build();
    }

    /**
     * 수정시 secretKey 세팅
     *
     * @param secretKey
     */
    public void setModifySecretKey(String secretKey) {
        if (this.secretKey == null || this.secretKey.contains("#")) {
            this.secretKey = secretKey;
        }
    }
}
