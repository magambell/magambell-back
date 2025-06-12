package com.magambell.server.user.app.port.in;

import com.magambell.server.user.app.port.in.request.UserSocialVerifyServiceRequest;
import com.magambell.server.user.app.port.in.request.VerifyEmailAuthCodeServiceRequest;
import com.magambell.server.user.app.port.in.request.VerifyEmailDuplicateServiceRequest;
import com.magambell.server.user.app.port.in.request.VerifyEmailSendServiceRequest;

public interface UserVerifyUseCase {
    void emailRegisterDuplicate(VerifyEmailDuplicateServiceRequest serviceRequest);

    void emailRegisterAuthCodeCheck(VerifyEmailAuthCodeServiceRequest serviceRequest);

    void emailRegisterSend(VerifyEmailSendServiceRequest serviceRequest);

    boolean verifySocialUser(UserSocialVerifyServiceRequest serviceRequest);
}
