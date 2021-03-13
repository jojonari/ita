package com.cafe24.apps.ita.dto;

import com.cafe24.apps.ita.entity.TimeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TextValue extends TimeEntity {
    private String text;
    private String value;
    private boolean disabled;

    public TextValue(String text, String value) {
        this.text = text;
        this.value = value;
    }
}
