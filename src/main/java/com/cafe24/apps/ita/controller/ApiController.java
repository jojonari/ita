package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.AccessTokenDto;
import com.cafe24.apps.ita.dto.ApiRequestDto;
import com.cafe24.apps.ita.dto.ResponseDto;
import com.cafe24.apps.ita.entity.ApiRequest;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.service.ApiService;
import com.cafe24.apps.ita.service.AppService;
import com.cafe24.apps.ita.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
    public ResponseDto callApi(@RequestBody ApiRequestDto apiRequestDto, HttpSession session) throws Exception {
        App app = appService.getApp(session, apiRequestDto.getClientId());
        AccessTokenDto accessTokenDto = authService.getAccessToken(app, apiRequestDto.getMallId());
        ResponseEntity<HashMap> response = apiService.callApi(apiRequestDto, accessTokenDto);
        ApiRequest apiRequest = new ApiRequest(apiRequestDto, response);
        apiService.saveApiRequest(apiRequest);
        return ResponseDto.success(apiRequest.convertDto());
    }

    @GetMapping("/apis")
    public ResponseDto getWebhooks(HttpSession session, Optional<String> clientId) {
        List<String> clientIds = appService.getAppClientIds(session, clientId);

        List<ApiRequestDto> apiRequestDtos = apiService.getApis(clientIds);
        return ResponseDto.success(apiRequestDtos);
    }
}
