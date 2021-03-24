package com.cafe24.apps.ita.dto;

import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.User;
import lombok.Builder;
import lombok.Data;

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

    /**
     * 인증 방법 확인
     * authorization_code : true
     *
     * @return
     */
    public boolean isAuthorizationCode() {
        return this.grantType.equals("authorization_code");
    }

    /**
     * 인증 방법 확인
     * client_credentials : true
     *
     * @return
     */
    public boolean isClientCredentials() {
        return this.grantType.equals("client_credentials");
    }

    /**
     * Authorization 조회
     */
    public String getAuthorization() {
        return String.format("Basic %s", new String(Base64.getEncoder().encode((this.clientId + ":" + this.secretKey).getBytes())));
    }

    /**
     * 토큰 갱신 여부
     *
     * @return
     */
    public boolean isRefresh() {
        return this.manageToken.equals("refresh");
    }


}
