package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.ResponseDto;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.service.AppService;
import com.cafe24.apps.ita.service.WebhookService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/webhook")
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
}
