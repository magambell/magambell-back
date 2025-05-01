package com.magambell.server.user.app.port.in.dto;

import com.magambell.server.user.domain.enums.VerificationStatus;
import com.magambell.server.user.domain.model.UserEmail;

public record UserEmailDTO(String email, String authCode, VerificationStatus verificationStatus) {

    public UserEmail toUserEmail() {
        return UserEmail.create(this);
    }
}
