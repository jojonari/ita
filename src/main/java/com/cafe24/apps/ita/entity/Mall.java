package com.cafe24.apps.ita.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
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
}
