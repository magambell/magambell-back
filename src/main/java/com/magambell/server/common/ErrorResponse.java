package com.magambell.server.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private int statusCode;
    private String name;
    private String code;
    private String message;
    private String path;
}
