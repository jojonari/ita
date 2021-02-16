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
import java.util.Set;

@Getter
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

    @CollectionTable(name = "t_app_scope")
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private Set<String> scopes;

    public void setUser(HttpSession session) {
        this.user = SessionUtil.getUserInfo(session);
    }

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
                .createdDate(this.createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedDate(this.modifiedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
