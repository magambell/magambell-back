package com.magambell.server.user.app.service;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.user.app.port.in.UserVerifyUseCase;
import com.magambell.server.user.app.port.in.request.VerifyEmailServiceRequest;
import com.magambell.server.user.app.port.out.UserQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserVerifyService implements UserVerifyUseCase {

    private final UserQueryPort userQueryPort;

    @Override
    public void emailSignupDuplicate(final VerifyEmailServiceRequest request) {
        if (userQueryPort.existsByEmail(request.email())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

}
