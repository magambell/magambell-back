package com.magambell.server.user.app.port.in;

import com.magambell.server.user.app.port.in.request.VerifyEmailAuthCodeServiceRequest;
import com.magambell.server.user.app.port.in.request.VerifyEmailServiceRequest;

public interface UserVerifyUseCase {
    void emailRegisterDuplicate(VerifyEmailServiceRequest serviceRequest);

    void emailRegisterAuthCodeCheck(VerifyEmailAuthCodeServiceRequest serviceRequest);
}
