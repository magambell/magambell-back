package com.magambell.server.common.exception;

import com.magambell.server.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends CustomException {

    public NotFoundException(ErrorCode errorCode) {

        this.errorCode = errorCode.name();
        this.name = "NotFoundException";
        this.message = errorCode.getMessage();
        this.statusCode = HttpStatus.BAD_REQUEST;
    }

}
