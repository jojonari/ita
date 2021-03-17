package com.cafe24.apps.ita.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResponseDto {

    private int code;
    private String message;
    private Object data;
    private Pageable pageable;

    private ResponseDto(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResponseDto success(Object data) {
        return new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK.name(), data);
    }

    public static ResponseDto success(Object data, Pageable pageable) {
        return new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK.name(), data, pageable);
    }

    public static ResponseDto badRequest(String message) {
        return new ResponseDto(HttpStatus.BAD_REQUEST.value(), message, null);
    }
}