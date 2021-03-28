package com.cafe24.apps.ita.entity;

import com.cafe24.apps.ita.dto.AccessTokenDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Entity
@ToString
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

    @Column(length = 64)
    private String refreshToken;

    @Column()
    private LocalDateTime refreshTokenExpiresAt;

    @Column(length = 16)
    private String mallId;

    @Column(length = 22, nullable = false)
    private String clientId;

    @Column(length = 16)
    private String userId;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @CollectionTable(name = "m_token_scopes")
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private Set<String> scopes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "m_app_token", joinColumns = @JoinColumn(name = "token_idx"), inverseJoinColumns = @JoinColumn(name = "app_idx"))
    private App app;

    @Builder
    public AccessToken(Long idx, String accessToken, LocalDateTime expiresAt, String refreshToken, LocalDateTime refreshTokenExpiresAt, String mallId, String clientId, String userId, LocalDateTime issuedAt, Set<String> scopes, App app) {
        this.idx = idx;
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
        this.mallId = mallId;
        this.clientId = clientId;
        this.userId = userId;
        this.issuedAt = issuedAt;
        this.scopes = scopes;
        this.app = app;
    }

    /**
     * convert DTO
     *
     * @return AccessTokenDto
     */
    public AccessTokenDto convertDto() {
        return AccessTokenDto.builder().
                access_token(this.accessToken)
                .issued_at(this.issuedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .expires_at(this.expiresAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .refresh_token(this.refreshToken)
                .refresh_token_expires_at(this.refreshTokenExpiresAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .mall_id(this.mallId)
                .client_id(this.clientId)
                .user_id(this.userId)
                .scopes(this.scopes)
                .build();
    }

    /**
     * convert DTO
     *
     * @return AccessTokenDto
     */
    public AccessTokenDto convertApiDto() {
        return AccessTokenDto.builder().
                access_token(this.accessToken)
                .issued_at(this.issuedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .expires_at(this.expiresAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .client_id(this.clientId)
                .scopes(this.scopes)
                .build();
    }
}
