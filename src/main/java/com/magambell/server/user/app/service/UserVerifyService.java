package com.magambell.server.user.app.service;

import static com.magambell.server.user.domain.enums.VerificationStatus.REGISTER;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.common.exception.NotEqualException;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.user.app.port.in.UserVerifyUseCase;
import com.magambell.server.user.app.port.in.dto.UserEmailDTO;
import com.magambell.server.user.app.port.in.request.VerifyEmailAuthCodeServiceRequest;
import com.magambell.server.user.app.port.in.request.VerifyEmailServiceRequest;
import com.magambell.server.user.app.port.out.UserEmailQueryPort;
import com.magambell.server.user.app.port.out.UserQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserVerifyService implements UserVerifyUseCase {

    private final UserQueryPort userQueryPort;

    private final UserEmailQueryPort userEmailQueryPort;

    @Override
    public void emailRegisterDuplicate(final VerifyEmailServiceRequest request) {
        if (userQueryPort.existsByEmail(request.email())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Override
    public void emailRegisterAuthCodeCheck(final VerifyEmailAuthCodeServiceRequest request) {
        validateEmailAndAuthCode(request.email(), request.authCode());
    }

    private void validateEmailAndAuthCode(final String email, final String authCode) {
        UserEmailDTO userEmailDTO = userEmailQueryPort.findRegisterUserEmail(email, REGISTER)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_EMAIL_NOT_FOUND));

        if (!userEmailDTO.authCode().equals(authCode)) {
            throw new NotEqualException(ErrorCode.USER_EMAIL_AUTH_CODE_NOT_EQUALS);
        }
    }

}
