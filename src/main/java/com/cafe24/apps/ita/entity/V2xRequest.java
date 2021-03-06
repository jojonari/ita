package com.cafe24.apps.ita.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class V2xRequest extends TimeEntity {

    @Id
    @GeneratedValue
    private Long idx;

    @Column(nullable = false)
    private Long userIdx;

    @Column(length = 30, nullable = false)
    private String clientId;

    @Column(length = 16, nullable = false)
    private String mallId;

    @Column(length = 10, nullable = false)
    private String method;

    @Column(length = 20)
    private String version;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String apiUrl;

    @Column(columnDefinition = "TEXT")
    private String requestBody;

    @Column(columnDefinition = "TEXT")
    private String response;

    @Builder
    public V2xRequest(Long idx, Long userIdx, String clientId, String mallId, String method, String version, String apiUrl, String requestBody, String response) {
        this.idx = idx;
        this.userIdx = userIdx;
        this.clientId = clientId;
        this.mallId = mallId;
        this.method = method;
        this.version = version;
        this.apiUrl = apiUrl;
        this.requestBody = requestBody;
        this.response = response;
    }
}
