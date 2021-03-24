package com.cafe24.apps.ita.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class MallDto {
    private Long idx;
    private String mallId;
    private String operationLevel;

    @Builder
    public MallDto(Long idx, String mallId, String operationLevel) {
        this.idx = idx;
        this.mallId = mallId;
        this.operationLevel = operationLevel;
    }
}

