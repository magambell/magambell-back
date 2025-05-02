package com.magambell.server.user.app.port.in;

import com.magambell.server.user.app.port.in.request.VerifyEmailServiceRequest;

public interface UserVerifyUseCase {
    void emailSignupDuplicate(VerifyEmailServiceRequest serviceRequest);
}
