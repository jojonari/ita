package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.Webhook;
import com.cafe24.apps.ita.repository.AccessTokenRepository;
import com.cafe24.apps.ita.repository.WebhookRepository;
import com.cafe24.apps.ita.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class WebhookService {
    /**
     * 삭제,만료 웹훅
     * 90077 : 삭제
     * 90078 : 만료
     */
    private final List<Integer> APP_DELETE_EVENTS = Arrays.asList(90077, 90078);

    private final WebhookRepository webhookRepository;
    private final AccessTokenRepository accessTokenRepository;

    public WebhookService(WebhookRepository webhookRepository, AccessTokenRepository accessTokenRepository) {
        this.webhookRepository = webhookRepository;
        this.accessTokenRepository = accessTokenRepository;
    }

    /**
     * webhook 단건 저장
     *
     * @param app
     * @param xTraceId
     * @param request
     */
    public void saveWebhook(App app, String xTraceId, HashMap<Object, Object> request) {
        Double eventNo = (Double) request.get("event_no");

        Webhook webhook = Webhook.builder()
                .app(app)
                .xTraceId(xTraceId)
                .eventNo(eventNo.intValue())
                .clientId(app.getClientId())
                .resource(JsonUtil.toJSON(request.get("resource")))
                .build();

        webhookRepository.save(webhook);
    }

    /**
     * exec app delete events
     * App event 중 access token 제거가 필요한 이벤트 처리
     *
     * @param request
     */
    public void execDeleteAppEvent(HashMap<Object, Object> request) {
        //앱 채널의 삭제, 만료이벤트일경우 토큰 삭제
        Double eventNo = (Double) request.get("event_no");
        if (this.APP_DELETE_EVENTS.contains(eventNo.intValue())) {
            HashMap<String, String> resource = (HashMap<String, String>) request.get("resource");
            accessTokenRepository.deleteByClientIdAndMallId(resource.get("client_id"), resource.get("mall_id"));
        }
    }
}
