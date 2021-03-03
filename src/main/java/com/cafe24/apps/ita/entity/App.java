package com.cafe24.apps.ita.entity;

import com.cafe24.apps.ita.dto.AppDto;
import com.cafe24.apps.ita.util.SessionUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Set;

@Entity
@Getter
@ToString(exclude = "secretKey")
@Table(name = "t_app")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class App extends TimeEntity {
    @Id
    @GeneratedValue
    private Long idx;

    @ManyToOne
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

    @Column(length = 15)
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
        this.user = SessionUtil.getUserInfo(session);
    }

    /**
     * Authorization 조회
     */
    public String getAuthorization() {
        return String.format("Basic %s", new String(Base64.getEncoder().encode((this.clientId + ":" + this.secretKey).getBytes())));
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
}
