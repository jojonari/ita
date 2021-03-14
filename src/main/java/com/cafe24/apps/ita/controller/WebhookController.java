package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.ResponseDto;
import com.cafe24.apps.ita.dto.WebhookDto;
import com.cafe24.apps.ita.dto.WebhookReciveDto;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.Webhook;
import com.cafe24.apps.ita.service.AppService;
import com.cafe24.apps.ita.service.WebhookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

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
    public ResponseDto saveWebhook(@PathVariable Long appIdx, @RequestHeader(value = "x-trace-id") String xTraceId, @RequestBody WebhookReciveDto webhookReciveDto) throws Exception {
        App app = appService.getApp(appIdx);
        webhookService.saveWebhook(webhookReciveDto.toEntity(app, xTraceId));
        webhookService.execDeleteAppEvent(webhookReciveDto);

        return ResponseDto.success(xTraceId);
    }

    @GetMapping
    public ResponseDto getWebhooks(HttpSession session, Optional<String> clientId, @PageableDefault(sort = "idx", direction = Sort.Direction.DESC, size = 100) Pageable pageable) {
        List<String> clientIds = appService.getAppClientIds(session, clientId);

        List<WebhookDto> webhooks = webhookService.getWebhooks(pageable, clientIds);
        return ResponseDto.success(webhooks);
    }

    @DeleteMapping("/{webhookIdx}")
    public ResponseDto deleteWebhook(@PathVariable Long webhookIdx) {
        Webhook webhook = webhookService.getWebhook(webhookIdx);
        if (webhook == null) {
            return ResponseDto.badRequest("수신된 webhook이 없습니다.");
        }

        webhookService.deleteWebhook(webhookIdx);
        return ResponseDto.success(null);
    }

    @DeleteMapping
    public ResponseDto deleteWebhook(HttpSession session) {
        List<String> clientIds = appService.getAppClientIds(session, Optional.empty());

        webhookService.deleteWebhooks(clientIds);
        return ResponseDto.success(null);
    }
}
