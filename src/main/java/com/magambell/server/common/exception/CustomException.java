package com.magambell.server.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException {
    protected String name;
    protected String errorCode;
    protected HttpStatus statusCode;
    protected String message;
}
