package com.magambell.server.common.exception;

import com.magambell.server.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(ErrorCode errorCode) {
        this.errorCode = errorCode.name();
        this.name = "UnauthorizedException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.FORBIDDEN;
    }
}
