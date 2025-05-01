package com.magambell.server.common.exception;

import com.magambell.server.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class DuplicateException extends CustomException {

    public DuplicateException(ErrorCode errorCode) {

        this.errorCode = errorCode.name();
        this.name = "DuplicateException";
        this.statusCode = HttpStatus.BAD_REQUEST;
        this.message = errorCode.getMessage();

    }
}
