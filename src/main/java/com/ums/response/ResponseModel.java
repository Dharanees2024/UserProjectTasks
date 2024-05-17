package com.ums.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResponseModel {
    private int status;
    private String message;
    private Object data;

    public static ResponseModel success(HttpStatus status, String message, Object data) {
        return new ResponseModel(status.value(), message, data);
    }
}
