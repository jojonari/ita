package com.cafe24.apps.ita.dto;

import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.Webhook;
import com.cafe24.apps.ita.util.JsonUtil;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Data;

@Data
public class WebhookReciveDto {

    private int event_no;
    private LinkedTreeMap<String, Object> resource;

    /**
     * toEntity
     *
     * @param app
     * @param xTraceId
     * @return
     */
    public Webhook toEntity(App app, String xTraceId) {
        return Webhook.builder()
                .app(app)
                .xTraceId(xTraceId)
                .eventNo(this.event_no)
                .clientId(app.convertDto().getClientId())
                .resource(JsonUtil.toJSON(this.resource))
                .build();
    }
}
