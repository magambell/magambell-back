package com.magambell.server.common.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "기본 응답")
public class BaseResponse {

    @Schema(description = "statusCode", example = "OK")
    protected int status;

    @Schema(description = "statusCode", example = "Success")
    protected int result;
}
