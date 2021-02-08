package com.cafe24.apps.ita.entity;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
public class TexeValue extends TimeEntity {

    private String text;
    private String value;
}
