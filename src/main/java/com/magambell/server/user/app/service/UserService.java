package com.magambell.server.user.app.service;

import static com.magambell.server.user.domain.enums.VerificationStatus.REGISTER;

import com.magambell.server.auth.app.service.JwtService;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.common.exception.NotEqualException;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.common.utility.SecurityUtility;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.user.adapter.out.persistence.UserInfoResponse;
import com.magambell.server.user.app.port.in.UserUseCase;
import com.magambell.server.user.app.port.in.dto.UserEmailDTO;
import com.magambell.server.user.app.port.in.request.LoginServiceRequest;
import com.magambell.server.user.app.port.in.request.RegisterServiceRequest;
import com.magambell.server.user.app.port.out.UserCommandPort;
import com.magambell.server.user.app.port.out.UserEmailQueryPort;
import com.magambell.server.user.app.port.out.UserInfoDTO;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService implements UserUseCase {

    private final UserQueryPort userQueryPort;
    private final UserCommandPort userCommandPort;
    private final UserEmailQueryPort userEmailQueryPort;
    private final JwtService jwtService;

    @Transactional
    public void register(RegisterServiceRequest request) {
        validateEmailAndAuthCode(request.email(), request.authCode());
        duplicatedEmail(request.email());

        String password = SecurityUtility.encodePassword(request.password());
        userEmailQueryPort.deleteEmail(request.email());
        userCommandPort.register(request.toCreateUserDTO(password));
    }

    @Override
    public void login(final LoginServiceRequest request) {
        String password = SecurityUtility.encodePassword(request.password());
        User user = userQueryPort.getUser(request.email(), password);

    }

    @Override
    public UserInfoResponse getUserInfo(final String token) {
        Long userId = jwtService.getJwtUserId(token);
        UserInfoDTO userInfoDTO = userQueryPort.getUserInfo(userId);
        validateOwner(userInfoDTO.userRole(), userInfoDTO.approved());
        return userInfoDTO.toResponse();
    }

    private void validateEmailAndAuthCode(final String email, final String authCode) {
        UserEmailDTO userEmailDTO = userEmailQueryPort.findRegisterUserEmail(email, REGISTER)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_EMAIL_NOT_FOUND));

        if (!userEmailDTO.authCode().equals(authCode)) {
            throw new NotEqualException(ErrorCode.USER_EMAIL_AUTH_CODE_NOT_EQUALS);
        }
    }

    private void duplicatedEmail(final String email) {
        if (userQueryPort.existsByEmail(email)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    private void validateOwner(final UserRole userRole, final Approved approved) {
        if (userRole == UserRole.OWNER && approved == null) {
            throw new NotFoundException(ErrorCode.OWNER_NOT_FOUND_STORE);
        }
    }
}
