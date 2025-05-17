package com.magambell.server.user.app.port.in.request;

public record LoginServiceRequest(
        String email,
        String password
) {
}
