package com.cafe24.apps.ita.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_webhook")
public class Webhook extends TimeEntity {

    @Id
    @GeneratedValue
    private Long idx;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "m_app_webhook", joinColumns = @JoinColumn(name = "app_idx"), inverseJoinColumns = @JoinColumn(name = "webhook_idx"))
    private App app;

    @Column(length = 30, nullable = false)
    private String clientId;

    @Column(length = 30, nullable = false)
    private double eventNo;

    @Column(length = 64, nullable = false)
    private String xTraceId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String resource;

    @Builder
    public Webhook(Long idx, App app, String clientId, double eventNo, String xTraceId, String resource) {
        this.idx = idx;
        this.app = app;
        this.clientId = clientId;
        this.eventNo = eventNo;
        this.xTraceId = xTraceId;
        this.resource = resource;
    }
}