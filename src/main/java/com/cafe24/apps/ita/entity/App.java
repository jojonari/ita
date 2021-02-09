package com.cafe24.apps.ita.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
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

    @Column(length = 30, nullable = false)
    private String appName;

    @Column(length = 30, nullable = false, unique = true)
    private String clientId;

    @Column(length = 30, nullable = false)
    private String secretKey;

    @Column(length = 30, nullable = false)
    private String partnerId;

    @Column(length = 30, nullable = false)
    private String grantType;

    @Column(length = 30, nullable = false)
    private String manageToken;

    @Column(length = 30, nullable = false)
    private String operationLevel;

    @CollectionTable(name = "t_app_scope")
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private Set<String> scopes;
}
