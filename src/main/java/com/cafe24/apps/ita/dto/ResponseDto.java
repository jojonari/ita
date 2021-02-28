package com.cafe24.apps.ita.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseDto {

    private int code;
    private String message;
    private Object data;

    private ResponseDto(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResponseDto success(Object data) {
        return new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK.name(), data);
    }

    public static ResponseDto badRequest(String message) {
        return new ResponseDto(HttpStatus.BAD_REQUEST.value(), message, null);
    }
}