package com.cafe24.apps.ita.entity;

import com.cafe24.apps.ita.dto.AppDto;
import com.cafe24.apps.ita.util.SessionUtil;
import lombok.*;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Getter
@Setter
@Entity
@ToString
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

    @Column(length = 30, unique = true)
    private String clientId;

    @Column(length = 30)
    private String secretKey;

    @Column(length = 30, nullable = false)
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
