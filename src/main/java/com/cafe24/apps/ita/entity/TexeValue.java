package com.cafe24.apps.ita.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TexeValue extends TimeEntity {
    private String text;
    private String value;
}
