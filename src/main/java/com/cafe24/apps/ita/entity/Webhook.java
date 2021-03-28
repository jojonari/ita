package com.cafe24.apps.ita.entity;

import com.cafe24.apps.ita.dto.WebhookDto;
import com.cafe24.apps.ita.util.JsonUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.format.DateTimeFormatter;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "t_webhook")
public class Webhook extends TimeEntity {

    @Id
    @GeneratedValue
    private Long idx;

    @Column(length = 22, nullable = false)
    private String clientId;

    @Column(length = 30, nullable = false)
    private int eventNo;

    @Column(length = 64, nullable = false)
    private String xTraceId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String resource;

    @Builder
    public Webhook(Long idx, String clientId, int eventNo, String xTraceId, String resource) {
        this.idx = idx;
        this.clientId = clientId;
        this.eventNo = eventNo;
        this.xTraceId = xTraceId;
        this.resource = resource;
    }

    /**
     * convert DTO
     *
     * @return WebhookDto
     */
    public WebhookDto convertDto() {
        return WebhookDto.builder().
                idx(this.idx)
                .clientId(this.clientId)
                .eventNo(this.eventNo)
                .xTraceId(this.xTraceId)
                .resource(JsonUtil.convertPrettyJson(this.resource))
                .createdDate(this.createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedDate(this.modifiedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
