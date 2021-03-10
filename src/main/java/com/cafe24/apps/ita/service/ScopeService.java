package com.cafe24.apps.ita.service;

import com.cafe24.apps.ita.dto.TexeValue;
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
    public List<TexeValue> getScopesOptions() {
        List<Scope> scopes = scopeRepository.findAll();
        List<TexeValue> texeValues = new ArrayList<>();
        for (Scope scope : scopes) {
            texeValues.add(scope.ToTextValue());
        }

        return texeValues;
    }
}
