package com.magambell.server.auth.app.service;

import com.magambell.server.auth.app.port.in.AuthUseCase;
import com.magambell.server.auth.app.port.in.request.SocialLoginServiceRequest;
import com.magambell.server.auth.domain.model.JwtToken;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.user.app.dto.OAuthUserInfo;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import com.magambell.server.user.app.port.out.OAuthClient;
import com.magambell.server.user.app.port.out.UserCommandPort;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService implements AuthUseCase {

    private final OAuthClient oAuthClient;
    private final UserQueryPort userQueryPort;
    private final UserCommandPort userCommandPort;
    private final JwtService jwtService;

    @Transactional
    @Override
    public JwtToken loginOrSignUp(final SocialLoginServiceRequest request) {
        validateUserRole(request.userRole());
        OAuthUserInfo userInfo = oAuthClient.getUserInfo(request.authCode());

        User user = userQueryPort.findUserBySocial(userInfo.providerType(),
                        userInfo.id())
                .orElseGet(() -> oAuthSignUp(userInfo, request.userRole()));

        return jwtService.createJwtToken(user.getId(), user.getUserRole());
    }

    private void validateUserRole(final UserRole userRole) {
        if (!userRole.isUserAssignable()) {
            throw new InvalidRequestException(ErrorCode.USER_ROLE_NOT_ASSIGNABLE);
        }
    }

    private User oAuthSignUp(final OAuthUserInfo userInfo, final UserRole userRole) {
        UserSocialAccountDTO userSocialAccountDTO = new UserSocialAccountDTO(userInfo.email(),
                userInfo.name(),
                userInfo.providerType(),
                userInfo.id(),
                userRole);

        return userCommandPort.registerBySocial(userSocialAccountDTO);
    }
}
