package com.magambell.server.user.app.port.in.request;

public record VerifyEmailAuthCodeServiceRequest(
        String email,
        String authCode
) {
}
