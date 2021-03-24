package com.cafe24.apps.ita.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ScopeDto {
    private Long idx;
    private String scope;
    private String scopeName;
    private String code;
    private String codeName;

    @Builder
    public ScopeDto(Long idx, String scope, String scopeName, String code, String codeName) {
        this.idx = idx;
        this.scope = scope;
        this.scopeName = scopeName;
        this.code = code;
        this.codeName = codeName;
    }

    public TextValue toTextValue() {
        return new TextValue(getText(), getValue());
    }

    private String getText() {
        return codeName + " ::: " + scopeName;
    }

    private String getValue() {
        return code;
    }
}

