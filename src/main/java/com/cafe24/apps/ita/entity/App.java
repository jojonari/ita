package com.cafe24.apps.ita.entity;

import com.cafe24.apps.ita.dto.AppDto;
import com.cafe24.apps.ita.dto.PrivateAppDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Entity
@ToString(exclude = "secretKey")
@Table(name = "t_app")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class App extends TimeEntity {
    @Id
    @GeneratedValue
    private Long idx;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn
    private User user;

    @Column(length = 30)
    private String appName;

    @Column(length = 22, unique = true)
    private String clientId;

    @Column(length = 22)
    private String secretKey;

    @Column(length = 16, nullable = false)
    private String partnerId;

    @Column(length = 30)//authorization_code, client_credentials
    private String grantType;

    @Column(length = 10)
    private String manageToken;

    @Column(length = 10)
    private String operationLevel;

    @CollectionTable(name = "m_app_scopes")
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private Set<String> scopes;

    /**
     * convert DTO
     *
     * @return
     */
    public AppDto convertDto() {
        return AppDto.builder().
                idx(this.idx)
                .appName(this.appName)
                .clientId(this.clientId)
                .partnerId(this.partnerId)
                .grantType(this.grantType)
                .manageToken(this.manageToken)
                .operationLevel(this.operationLevel)
                .scopes(this.scopes)
                .modifiedDate(this.modifiedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    /**
     * convert DTO
     *
     * @return
     */
    public PrivateAppDto convertPrivateDto() {
        return PrivateAppDto.builder().
                idx(this.idx)
                .appName(this.appName)
                .clientId(this.clientId)
                .secretKey(this.secretKey)
                .partnerId(this.partnerId)
                .grantType(this.grantType)
                .manageToken(this.manageToken)
                .operationLevel(this.operationLevel)
                .scopes(this.scopes)
                .build();
    }

    @Builder
    public App(Long idx, User user, String appName, String clientId, String secretKey, String partnerId, String grantType, String manageToken, String operationLevel, Set<String> scopes) {
        this.idx = idx;
        this.user = user;
        this.appName = appName;
        this.clientId = clientId;
        this.secretKey = secretKey;
        this.partnerId = partnerId;
        this.grantType = grantType;
        this.manageToken = manageToken;
        this.operationLevel = operationLevel;
        this.scopes = scopes;
    }
}
