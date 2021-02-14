package com.cafe24.apps.ita.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TexeValue extends TimeEntity {

    private String text;
    private String value;
}
