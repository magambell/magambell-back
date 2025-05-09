package com.magambell.server.user.app.port.in;

import com.magambell.server.user.domain.enums.VerificationStatus;

public interface AwsEmailServiceInputPort {
    void sendEmail(String to, String authCode, VerificationStatus verificationStatus);
}
