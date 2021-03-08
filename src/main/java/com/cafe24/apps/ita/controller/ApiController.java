package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.ApiDto;
import com.cafe24.apps.ita.dto.ResponseDto;
import com.cafe24.apps.ita.entity.AccessToken;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.service.ApiService;
import com.cafe24.apps.ita.service.AppService;
import com.cafe24.apps.ita.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final ApiService apiService;
    private final AppService appService;
    private final AuthService authService;
    public ApiController(ApiService apiService, AppService appService, AuthService authService) {
        this.apiService = apiService;
        this.appService = appService;
        this.authService = authService;
    }

    @PostMapping("/api")
    public ResponseDto callApi(@RequestBody ApiDto apiDto, HttpSession session) throws Exception {
        App app = appService.getApp(session, apiDto.getClientId());
        AccessToken accessToken = authService.getAccessToken(app, apiDto.getMallId());
        System.out.println(accessToken);
        apiService.callApi(apiDto);
        return ResponseDto.success(apiDto);
    }
}
