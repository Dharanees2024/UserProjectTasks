package com.ums.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String errorMessage;
    private Map<String, String> fieldErrors;
    public ErrorResponse(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
