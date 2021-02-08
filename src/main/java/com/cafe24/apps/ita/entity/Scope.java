package com.cafe24.apps.ita.entity;

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
    private String scopeCode;

    @Column(length = 30, nullable = false)
    private String scopeName;

    @Column(length = 30, nullable = false)
    private String authCode;

    @Column(length = 30, nullable = false)
    private String authName;

    @Builder
    public Scope(Long idx, String scopeCode, String scopeName, String authCode, String authName) {
        this.idx = idx;
        this.scopeCode = scopeCode;
        this.scopeName = scopeName;
        this.authCode = authCode;
        this.authName = authName;
    }

    public TexeValue  ToTextValue(){
        return new TexeValue(getText(), getValue());
    }


    private String getText() {
        return authName + " : " + scopeName;
    }

    private String getValue() {
        return authCode;
    }
}
