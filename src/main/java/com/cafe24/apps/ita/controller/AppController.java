package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.AppDto;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.entity.Response;
import com.cafe24.apps.ita.service.AppService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class AppController {
    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping("/app")
    public Response saveApp(@RequestBody App app, HttpSession session) {
        App result = appService.getApp(app.getClientId());
        if (result != null) {
            return Response.badRequest("이미 등록된 ClientId 입니다.");
        }

        app.setUser(session);
        App saveApp = appService.saveApp(app);
        return Response.success(saveApp.convertDto());
    }

    @GetMapping("/apps")
    public Response getApps(HttpSession session, Optional<String> clientId) {
        List<AppDto> appDtos = appService.getApps(session, clientId);

        if (appDtos == null) {
            return Response.badRequest("App 리스트 조회에 실패했습니다.");
        }

        return Response.success(appDtos);
    }
}
