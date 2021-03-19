package com.cafe24.apps.ita.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mall extends TimeEntity {
    @Id
    @GeneratedValue
    private Long idx;

    @Column(length = 16, nullable = false)
    private String mallId;

    @Column(length = 10, nullable = false)
    private String operationLevel;
}
