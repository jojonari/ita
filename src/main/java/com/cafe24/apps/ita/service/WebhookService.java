package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.AppDto;
import com.cafe24.apps.ita.dto.WebhookDto;
import com.cafe24.apps.ita.dto.WebhookReciveDto;
import com.cafe24.apps.ita.entity.Webhook;
import com.cafe24.apps.ita.repository.AccessTokenRepository;
import com.cafe24.apps.ita.repository.WebhookRepository;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
     * @param webhook
     */
    public void saveWebhook(Webhook webhook) {
        webhookRepository.save(webhook);
    }

    /**
     * exec app delete events
     * App event 중 access token 제거가 필요한 이벤트 처리
     *
     * @param request
     */
    public void execDeleteAppEvent(WebhookReciveDto request) {
        //앱 채널의 삭제, 만료이벤트일경우 토큰 삭제
        if (this.APP_DELETE_EVENTS.contains(request.getEvent_no())) {
            LinkedTreeMap<String, Object> resource = request.getResource();
            accessTokenRepository.deleteByClientIdAndMallId(resource.get("client_id").toString(), resource.get("mall_id").toString());
        }
    }

    /**
     * webhooks 조회
     *
     * @param clientIds
     * @return
     */
    public List<WebhookDto> getWebhooks(List<String> clientIds) {
        List<Webhook> webhooks = webhookRepository.findAllByClientIdIn(clientIds);

        return webhooks.stream().map(Webhook::convertDto).collect(Collectors.toList());
    }

    /**
     * 웹훅 조회
     *
     * @param webhookIdx
     * @return
     */
    public Webhook getWebhook(Long webhookIdx) {
        return webhookRepository.getOne(webhookIdx);
    }

    /**
     * 웹훅 단건 삭제
     *
     * @param webhookIdx
     */
    public void deleteWebhook(Long webhookIdx) {
        webhookRepository.deleteById(webhookIdx);
    }

    /**
     * 웹훅 삭제
     *
     * @param clientIds
     */
    public void deleteWebhooks( List<String> clientIds) {
        webhookRepository.deleteByClientIdIn(clientIds);
    }
}
