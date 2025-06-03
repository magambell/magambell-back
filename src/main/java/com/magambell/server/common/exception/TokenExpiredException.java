package com.magambell.server.common.exception;

import com.magambell.server.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class TokenExpiredException extends CustomException {

    public TokenExpiredException(final ErrorCode errorCode) {
        this.errorCode = errorCode.name();
        this.name = "TokenExpiredException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.UNAUTHORIZED;
    }
}
