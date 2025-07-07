package com.magambell.server.common.utility;

import com.magambell.server.common.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ErrorResponseUtility {
    public static void writeErrorResponse(HttpServletRequest request, HttpServletResponse response,
                                          CustomException exception) throws IOException {
        response.setStatus(exception.getStatusCode().value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json;charset=UTF-8");

        String json = String.format(
                "{\"statusCode\": %d, \"name\": \"%s\", \"code\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
                exception.getStatusCode().value(),
                exception.getName(),
                exception.getErrorCode(),
                exception.getMessage(),
                request.getRequestURI()
        );

        response.getWriter().write(json);
    }
}
