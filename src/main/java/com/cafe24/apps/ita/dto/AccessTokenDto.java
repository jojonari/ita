package com.cafe24.apps.ita.dto;

import com.cafe24.apps.ita.entity.AccessToken;
import com.cafe24.apps.ita.entity.App;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;


@Data
@Builder
@ToString
public class AccessTokenDto {

    private String access_token;
    private String expires_at;
    private String refresh_token;
    private String refresh_token_expires_at;
    private String mall_id;
    private String client_id;
    private String user_id;
    private Set<String> scopes;
    private String issued_at;

    /**
     * to AccessToken Entity
     *
     * @return
     */
    public AccessToken toEntity(App app) {
        return AccessToken.builder()
                .accessToken(this.access_token)
                .expiresAt(LocalDateTime.parse(this.expires_at))
                .refreshToken(this.refresh_token)
                .refreshTokenExpiresAt(LocalDateTime.parse(this.refresh_token_expires_at))
                .mallId(this.mall_id)
                .clientId(this.client_id)
                .userId(this.user_id)
                .scopes(this.scopes)
                .app(app)
                .issuedAt(LocalDateTime.parse(this.issued_at))
                .build();
    }

    /**
     * to AccessToken Entity
     *
     * @return
     */
    public AccessToken toClientCredentialsEntity(App app) {
        return AccessToken.builder()
                .accessToken(this.access_token)
                .expiresAt(this.getExpiresAtLocalDateTime())
                .clientId(this.client_id)
                .scopes(this.scopes)
                .issuedAt(this.getIssuedAtLocalDateTime())
                .app(app)
                .build();
    }

    public LocalDateTime getExpiresAtLocalDateTime() {
        String pattern = this.expires_at.indexOf("T") > 0 ? "yyyy-MM-dd'T'HH:mm:ss.SSS": "yyyy-MM-dd HH:mm:ss";
        return LocalDateTime.parse(this.expires_at, DateTimeFormatter.ofPattern(pattern));
    }

    public LocalDateTime getIssuedAtLocalDateTime() {
        String pattern = this.issued_at.indexOf("T") > 0 ? "yyyy-MM-dd'T'HH:mm:ss.SSS": "yyyy-MM-dd HH:mm:ss";
        return LocalDateTime.parse(this.issued_at, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * convertTextValueSetMallId
     *
     * @return TextValue
     */
    public TextValue convertTextValueSetMallId() {
        if (this.isRefreshTokenExpire()) {
            return new TextValue(this.mall_id, this.mall_id, true);
        }

        return new TextValue(this.mall_id, this.mall_id);
    }

    /**
     * Access token 만료 조회
     * 만료 : true
     *
     * @return Boolean
     */
    public Boolean isAccessTokenExpire() {
        return LocalDateTime.now().isAfter(LocalDateTime.parse(this.expires_at, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    /**
     * Refresh token 만료 조회
     * 만료 : true
     *
     * @return Boolean
     */
    public Boolean isRefreshTokenExpire() {
        return LocalDateTime.now().isAfter(LocalDateTime.parse(this.refresh_token_expires_at, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

}
