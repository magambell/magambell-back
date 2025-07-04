package com.magambell.server.user.app.port.in.request;

public record UserEditServiceRequest(
        String nickName,
        Long userId
) {
}
