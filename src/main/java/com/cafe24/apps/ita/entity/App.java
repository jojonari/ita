package com.cafe24.apps.ita.entity;

import com.cafe24.apps.ita.dto.AppDto;
import com.cafe24.apps.ita.dto.PrivateAppDto;
import com.cafe24.apps.ita.util.SessionUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Set;

@Entity
@ToString(exclude = "secretKey")
@Table(name = "t_app")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class App extends TimeEntity {
    @Id
    @GeneratedValue
    private Long idx;

    @ManyToOne(cascade = CascadeType.ALL)
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setUser(HttpSession session) {
        this.user = SessionUtil.getUserInfo(session).toEntity();
    }

    public String getOperationLevel() {
        return operationLevel;
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

    /**
     * 인증 방법 확인
     * authorization_code : true
     *
     * @return
     */
    public boolean isAuthorizationCode() {
        return this.grantType.equals("authorization_code");
    }
}
