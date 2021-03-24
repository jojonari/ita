package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.ScopeDto;
import com.cafe24.apps.ita.dto.TextValue;
import com.cafe24.apps.ita.entity.Scope;
import com.cafe24.apps.ita.repository.ScopeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScopeService {

    private final ScopeRepository scopeRepository;

    public ScopeService(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    /**
     * 스코프 옵션 조회
     *
     * @return
     */
    public List<TextValue> getScopesOptions() {
        List<Scope> scopes = scopeRepository.findAll();
        return scopes.stream()
                .map(Scope::convertDto)
                .map(ScopeDto::toTextValue)
                .collect(Collectors.toList());
    }
}
