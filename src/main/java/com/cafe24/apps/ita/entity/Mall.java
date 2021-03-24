package com.cafe24.apps.ita.entity;

import com.cafe24.apps.ita.dto.MallDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Mall extends TimeEntity {
    @Id
    @GeneratedValue
    private Long idx;

    @Column(length = 16, nullable = false)
    private String mallId;

    @Column(length = 10, nullable = false)
    private String operationLevel;

    public MallDto convertDto() {
        return MallDto.builder()
                .idx(this.idx)
                .mallId(this.mallId)
                .operationLevel(this.operationLevel)
                .build();
    }
}
