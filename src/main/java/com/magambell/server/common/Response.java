package com.magambell.server.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Response<T> {

    private HttpStatus status;
    private T data;

    public Response() {
        this.status = HttpStatus.OK;
        this.data = (T) "success";
    }

    public Response(T data) {
        this.status = HttpStatus.OK;
        this.data = data;
    }

    public Response(HttpStatus status, T data) {
        this.status = status;
        this.data = data;
    }
}
