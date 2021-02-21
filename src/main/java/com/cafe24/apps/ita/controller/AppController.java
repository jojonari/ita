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

    @GetMapping("/apps")
    public Response getApps(HttpSession session, Optional<String> clientId) {
        List<AppDto> appDtos = appService.getApps(session, clientId);

        if (appDtos == null) {
            return Response.badRequest("App 리스트 조회에 실패했습니다.");
        }

        return Response.success(appDtos);
    }

    @PostMapping("/app")
    public Response registerApp(@RequestBody App app, HttpSession session) {
        App result = appService.getApp(app.getClientId());
        if (result != null) {
            return Response.badRequest("이미 등록된 ClientId 입니다.");
        }

        app.setUser(session);
        App registerApp = appService.registerApp(app);
        return Response.success(registerApp.convertDto());
    }

    @PutMapping("/app/{appIdx}")
    public Response modifyApp(HttpSession session, @PathVariable Long appIdx, @RequestBody App app) {
        App result = appService.getApp(session, appIdx);
        if (result == null) {
            return Response.badRequest("등록된 Client가 없습니다.");
        }

        app.setUser(session);
        app.setModifySecretKey(result.getSecretKey());

        App modifyApp = appService.modifyApp(app);
        return Response.success(modifyApp.convertDto());
    }
    @DeleteMapping("/app/{appIdx}")
    public Response deleteApp(HttpSession session, @PathVariable Long appIdx) {
        App app = appService.getApp(session, appIdx);
        if (app == null) {
            return Response.badRequest("등록된 Client가 없습니다.");
        }

        appService.deleteApp(appIdx);
        return Response.success(null);
    }
}
