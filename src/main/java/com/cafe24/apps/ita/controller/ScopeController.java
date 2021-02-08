package com.cafe24.apps.ita.controller;

import com.cafe24.apps.ita.entity.TexeValue;
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

    @GetMapping("/scopes")
    public List<TexeValue> getScopeBase() {
        System.out.println("getScopeBase");
        return scopeService.getScopeBase();
    }
}
