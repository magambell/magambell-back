package com.magambell.server.user.app.service;

import static com.magambell.server.user.domain.enums.VerificationStatus.REGISTER;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.common.exception.NotEqualException;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.user.app.port.in.UserUseCase;
import com.magambell.server.user.app.port.in.dto.UserEmailDTO;
import com.magambell.server.user.app.port.in.request.RegisterServiceRequest;
import com.magambell.server.user.app.port.out.UserEmailQueryPort;
import com.magambell.server.user.app.port.out.UserQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService implements UserUseCase {

    private final UserQueryPort userQueryPort;
    private final UserEmailQueryPort userEmailQueryPort;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void register(RegisterServiceRequest request) {
        validateEmailAndAuthCode(request.email(), request.authCode());
        validatePassword(request.password());
        duplicatedEmail(request.email());

        userEmailQueryPort.deleteEmail(request.email());
        userQueryPort.register(request.toCreateUserDTO(bCryptPasswordEncoder.encode(request.password())));
    }

    private void validateEmailAndAuthCode(final String email, final String authCode) {
        UserEmailDTO userEmailDTO = userEmailQueryPort.findRegisterUserEmail(email, REGISTER)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_EMAIL_NOT_FOUND));

        if (!userEmailDTO.authCode().equals(authCode)) {
            throw new NotEqualException(ErrorCode.USER_EMAIL_AUTH_CODE_NOT_EQUALS);
        }
    }

    private void validatePassword(final String password) {
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,16}$")) {
            throw new InvalidRequestException(ErrorCode.USER_VALID_PASSWORD);
        }
    }

    private void duplicatedEmail(final String email) {
        if (userQueryPort.existsByEmail(email)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
    }
}
