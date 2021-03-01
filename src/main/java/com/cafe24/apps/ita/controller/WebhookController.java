package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.AppDto;
import com.cafe24.apps.ita.dto.ResponseDto;
import com.cafe24.apps.ita.dto.WebhookDto;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.Webhook;
import com.cafe24.apps.ita.service.AppService;
import com.cafe24.apps.ita.service.WebhookService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/webhooks")
public class WebhookController {

    private final WebhookService webhookService;
    private final AppService appService;

    public WebhookController(WebhookService webhookService, AppService appService) {
        this.webhookService = webhookService;
        this.appService = appService;
    }

    @PostMapping("/{appIdx}")
    public ResponseDto saveWebhook(@PathVariable Long appIdx, @RequestHeader(value = "x-trace-id") String xTraceId, @RequestBody HashMap<Object, Object> request) throws Exception {
        App app = appService.getApp(appIdx);

        webhookService.saveWebhook(app, xTraceId, request);

        webhookService.execDeleteAppEvent(request);

        return ResponseDto.success(xTraceId);
    }

    @GetMapping
    public ResponseDto getWebhooks(HttpSession session, Optional<String> clientId) {
        List<String> clientIds = appService.getAppClientIds(session, clientId);

        List<WebhookDto> webhooks = webhookService.getWebhooks(clientIds);

        return ResponseDto.success(webhooks);
    }
}
