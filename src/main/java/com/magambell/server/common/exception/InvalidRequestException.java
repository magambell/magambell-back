package com.magambell.server.common.exception;

import com.magambell.server.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidRequestException extends CustomException {

    public InvalidRequestException(ErrorCode errorCode) {

        this.errorCode = errorCode.name();
        this.name = "InvalidRequestException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.BAD_REQUEST;

    }
}
