package com.cafe24.apps.ita.entity;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Response {

    private int code;
    private String message;
    private Object data;

    private Response(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Response success(Object data) {
        return new Response(HttpStatus.OK.value(), HttpStatus.OK.name(), data);
    }

    public static Response badRequest(String message) {
        return new Response(HttpStatus.BAD_REQUEST.value(), message, null);
    }
}