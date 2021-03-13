package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.TextValue;
import com.cafe24.apps.ita.entity.Scope;
import com.cafe24.apps.ita.repository.ScopeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<TextValue> textValues = new ArrayList<>();
        for (Scope scope : scopes) {
            textValues.add(scope.ToTextValue());
        }

        return textValues;
    }
}
