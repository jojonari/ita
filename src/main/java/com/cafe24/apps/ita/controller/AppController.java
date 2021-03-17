package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.AppDto;
import com.cafe24.apps.ita.dto.PrivateAppDto;
import com.cafe24.apps.ita.dto.ResponseDto;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.service.AppService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseDto getApps(HttpSession session, Optional<String> clientId, @PageableDefault(sort = "idx", direction = Sort.Direction.DESC, size = 20) Pageable pageable) {
        List<AppDto> appDtos = appService.getApps(session, pageable, clientId);

        if (appDtos == null) {
            return ResponseDto.badRequest("App 리스트 조회에 실패했습니다.");
        }

        return ResponseDto.success(appDtos, pageable);
    }

    @PostMapping("/app")
    public ResponseDto registerApp(@RequestBody PrivateAppDto app, HttpSession session) {
        App result = appService.getApp(app.getClientId());
        if (result != null) {
            return ResponseDto.badRequest("이미 등록된 ClientId 입니다.");
        }

        app.setUser(session);
        App registerApp = appService.registerApp(app.toEntity());
        return ResponseDto.success(registerApp.convertDto());
    }

    @PutMapping("/app/{appIdx}")
    public ResponseDto modifyApp(@PathVariable Long appIdx, @RequestBody PrivateAppDto privateAppDto, HttpSession session) {
        App result = appService.getApp(session, appIdx);
        if (result == null) {
            return ResponseDto.badRequest("등록된 client가 없습니다.");
        }

        privateAppDto.setUser(session);
        privateAppDto.setModifySecretKey(result.convertPrivateDto().getSecretKey());

        App modifyApp = appService.modifyApp(privateAppDto.toEntity());
        return ResponseDto.success(modifyApp.convertDto());
    }

    @DeleteMapping("/app/{appIdx}")
    public ResponseDto deleteApp(@PathVariable Long appIdx, HttpSession session) {
        App app = appService.getApp(session, appIdx);
        if (app == null) {
            return ResponseDto.badRequest("등록된 client가 없습니다.");
        }

        appService.deleteApp(appIdx);
        return ResponseDto.success(null);
    }
}
