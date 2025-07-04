package com.magambell.server.common.exception;

import com.magambell.server.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class InternalServerException extends CustomException {

    public InternalServerException(ErrorCode errorCode) {
        this.errorCode = errorCode.name();
        this.name = "InternalServerException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
