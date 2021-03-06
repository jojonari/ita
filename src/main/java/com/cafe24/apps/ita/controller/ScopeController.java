package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.dto.ResponseDto;
import com.cafe24.apps.ita.dto.TexeValue;
import com.cafe24.apps.ita.service.ScopeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ScopeController {
    private final ScopeService scopeService;

    public ScopeController(ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    @GetMapping("/scopes/options")
    public ResponseDto getScopesOptions() {
        List<TexeValue> scopeOptions = scopeService.getScopesOptions();
        if (scopeOptions == null) {
            return ResponseDto.badRequest("스코프 옵션 조회에 실패했습니다.");
        }

        return ResponseDto.success(scopeOptions);
    }
}
