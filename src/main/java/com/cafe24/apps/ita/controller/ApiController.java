package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.AccessTokenDto;
import com.cafe24.apps.ita.dto.ApiRequestDto;
import com.cafe24.apps.ita.dto.ResponseDto;
import com.cafe24.apps.ita.dto.TextValue;
import com.cafe24.apps.ita.entity.ApiRequest;
import com.cafe24.apps.ita.entity.App;
import com.cafe24.apps.ita.service.ApiService;
import com.cafe24.apps.ita.service.AppService;
import com.cafe24.apps.ita.service.AuthService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
        boolean resultVlid = apiService.apiValid(app, apiRequestDto, session);
        if (!resultVlid) {
            return ResponseDto.badRequest("부여된 권한 내에서만 사용가능합니다.");
        }

        AccessTokenDto accessTokenDto = authService.getAccessToken(app, apiRequestDto.getMallId());
        ResponseEntity<HashMap> response = apiService.callApi(apiRequestDto, accessTokenDto);
        ApiRequest apiRequest = new ApiRequest(apiRequestDto, response);
        apiService.saveApiRequest(apiRequest);
        return ResponseDto.success(apiRequest.convertDto());
    }

    @GetMapping("/apis")
    public ResponseDto getApis(HttpSession session, Optional<String> clientId, @PageableDefault(sort = "idx", direction = Sort.Direction.DESC) Pageable pageable) {
        List<String> clientIds = appService.getAppClientIds(session, clientId);

        List<ApiRequestDto> apiRequestDtos = apiService.getApis(pageable, clientIds);
        return ResponseDto.success(apiRequestDtos, pageable);
    }

    @DeleteMapping("/api/{apiIdx}")
    public ResponseDto deleteApi(@PathVariable Long apiIdx) {
        ApiRequest apiRequest = apiService.getApi(apiIdx);
        if (apiRequest == null) {
            return ResponseDto.badRequest("api 요청이 없습니다.");
        }

        apiService.deleteApi(apiRequest);
        return ResponseDto.success(null);
    }

    @DeleteMapping("/apis")
    public ResponseDto deleteApiAll(HttpSession session) {
        List<String> clientIds = appService.getAppClientIds(session, Optional.empty());

        apiService.deleteApis(clientIds);

        return ResponseDto.success(null);
    }

    @GetMapping("/api/{clientId}/mallIds")
    public ResponseDto getApiMallIds(HttpSession session, @PathVariable String clientId) {
        App app = appService.getApp(session, clientId);
        List<TextValue> textValues;
        if (app.isAuthorizationCode()) {
            textValues = authService.getTextValuesSetMallId(app);
            return ResponseDto.success(textValues);
        }

        textValues = authService.getTextValuesSetMallIdByOperationLevel(app);
        return ResponseDto.success(textValues);

    }
}
