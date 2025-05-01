package com.magambell.server.common.exception;


import com.magambell.server.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotEqualException extends CustomException {

    public NotEqualException(ErrorCode errorCode) {

        this.errorCode = errorCode.name();
        this.name = "NotEqualException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.BAD_REQUEST;
    }

}
