package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.Response;
import com.cafe24.apps.ita.service.AppService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AppController {
    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping("/app")
    public Response saveApp(@RequestBody App app) {
        App result = appService.getApp(app.getClientId());
        if (result != null) {
            return Response.badRequest("이미 등록된 ClientId 입니다.");
        }

        App saveApp = appService.saveApp(app);
        return Response.success(saveApp);
    }
}
