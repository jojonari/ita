package com.cafe24.apps.ita.entity;

import com.cafe24.apps.ita.dto.ScopeDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "t_scope")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scope extends TimeEntity {

    @Id
    @GeneratedValue
    private Long idx;

    @Column(length = 30, nullable = false)
    private String scope;

    @Column(length = 30, nullable = false)
    private String scopeName;

    @Column(length = 30, nullable = false)
    private String code;

    @Column(length = 30, nullable = false)
    private String codeName;

    @Builder
    public Scope(Long idx, String scope, String scopeName, String code, String codeName) {
        this.idx = idx;
        this.scope = scope;
        this.scopeName = scopeName;
        this.code = code;
        this.codeName = codeName;
    }

    /**
     * convertDto
     *
     * @return
     */
    public ScopeDto convertDto() {
        return ScopeDto.builder()
                .idx(idx)
                .scope(scope)
                .scopeName(scopeName)
                .code(code)
                .codeName(codeName)
                .build();
    }
}
