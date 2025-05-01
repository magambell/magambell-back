package com.magambell.server.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private HttpStatus status;
    private T data;

    public ApiResponse() {
        this.status = HttpStatus.OK;
        this.data = (T) "success";
    }

    public ApiResponse(T data) {
        this.status = HttpStatus.OK;
        this.data = data;
    }

    public ApiResponse(HttpStatus status, T data) {
        this.status = status;
        this.data = data;
    }
}
