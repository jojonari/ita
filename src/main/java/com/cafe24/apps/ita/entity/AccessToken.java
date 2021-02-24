package com.cafe24.apps.ita.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_access_token", uniqueConstraints = @UniqueConstraint(columnNames = {"clientId", "mallId"}))
public class AccessToken extends TimeEntity {

    @Id
    @GeneratedValue
    private Long idx;

    @Column(length = 64, nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(length = 64, nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private LocalDateTime refreshTokenExpiresAt;

    @Column(length = 16, nullable = false)
    private String mallId;

    @Column(length = 30, nullable = false)
    private String clientId;

    @Column(length = 16, nullable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @CollectionTable(name = "m_token_scopes")
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private Set<String> scopes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "m_app_token", joinColumns = @JoinColumn(name = "app_idx"), inverseJoinColumns = @JoinColumn(name = "token_idx"))
    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    /**
     * Access token 만료 조회
     * 만료 : true
     *
     * @return
     */
    public Boolean isAccessTokenExpire() {
        return LocalDateTime.now().isBefore(expiresAt);
    }

    /**
     * Refresh token 만료 조회
     * 만료 : true
     *
     * @return
     */
    public Boolean isRefreshTokenExpire() {
        return LocalDateTime.now().isBefore(refreshTokenExpiresAt);
    }
}
