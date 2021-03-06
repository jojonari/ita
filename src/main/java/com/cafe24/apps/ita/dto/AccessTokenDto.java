package com.cafe24.apps.ita.dto;

import com.cafe24.apps.ita.entity.AccessToken;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
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
    public AccessToken toEntity() {
        return AccessToken.builder()
                .accessToken(this.access_token)
                .expiresAt(LocalDateTime.parse(this.expires_at))
                .refreshToken(this.refresh_token)
                .refreshTokenExpiresAt(LocalDateTime.parse(this.refresh_token_expires_at))
                .mallId(this.mall_id)
                .clientId(this.client_id)
                .userId(this.user_id)
                .scopes(this.scopes)
                .issuedAt(LocalDateTime.parse(this.issued_at))
                .build();
    }

}
