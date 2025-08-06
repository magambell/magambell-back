package com.magambell.server.user.adapter.in.web;

import com.magambell.server.user.app.port.in.request.UserEditServiceRequest;

public record UserEditRequest(
        String nickName
) {
    public UserEditServiceRequest toServiceRequest(Long userId) {
        return new UserEditServiceRequest(nickName, userId);
    }
}
