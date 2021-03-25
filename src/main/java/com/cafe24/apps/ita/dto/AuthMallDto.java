package com.cafe24.apps.ita.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class AuthMallDto {

    private String is_multi_shop;
    private String lang;
    private String nation;
    @NotEmpty(message = "mall_id는 필수 값입니다.")
    private String mall_id;
    private String shop_no;
    @Positive(message = "timestamp는 필수 값입니다.")
    private long timestamp;
    private String user_id;
    private String user_name;
    private String user_type;
    @NotEmpty(message = "hmac는 필수 값입니다.")
    private String hmac;
}
