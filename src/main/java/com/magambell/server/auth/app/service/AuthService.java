package com.magambell.server.auth.app.service;

import com.magambell.server.auth.app.port.in.AuthUseCase;
import com.magambell.server.auth.app.port.in.request.SocialLoginServiceRequest;
import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.auth.domain.model.JwtToken;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.user.app.dto.OAuthUserInfo;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import com.magambell.server.user.app.port.out.OAuthClient;
import com.magambell.server.user.app.port.out.UserCommandPort;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.model.User;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthService implements AuthUseCase {

    private final Map<ProviderType, OAuthClient> oAuthClientMap;
    private final UserQueryPort userQueryPort;
    private final UserCommandPort userCommandPort;
    private final JwtService jwtService;

    public AuthService(final List<OAuthClient> oAuthClients, final UserQueryPort userQueryPort,
                       final UserCommandPort userCommandPort,
                       final JwtService jwtService) {
        this.oAuthClientMap = oAuthClients.stream()
                .collect(Collectors.toMap(OAuthClient::getProviderType, Function.identity()));
        this.userQueryPort = userQueryPort;
        this.userCommandPort = userCommandPort;
        this.jwtService = jwtService;
    }

    @Transactional
    @Override
    public JwtToken loginOrSignUp(final SocialLoginServiceRequest request) {
        OAuthClient oAuthClient = oAuthClientMap.get(request.providerType());
        OAuthUserInfo userInfo = oAuthClient.getUserInfo(request.authCode());

        User user = userQueryPort.findUserBySocial(userInfo.providerType(),
                        userInfo.id())
                .orElseGet(() -> oAuthSignUp(userInfo, request.userRole(), request.nickName(), request.phoneNumber()));

        return jwtService.createJwtToken(user.getId(), user.getUserRole());
    }

    private User oAuthSignUp(final OAuthUserInfo userInfo, final UserRole userRole, final String nickName,
                             final String phoneNumber) {
        String finalPhoneNumber = validatePhoneNumber(userInfo, phoneNumber);

        validateSignUpFields(nickName, userRole);
        validateUserRole(userRole);

        UserSocialAccountDTO userSocialAccountDTO = new UserSocialAccountDTO(userInfo.email(),
                userInfo.name(),
                nickName,
                finalPhoneNumber,
                userInfo.providerType(),
                userInfo.id(),
                userRole);

        return userCommandPort.registerBySocial(userSocialAccountDTO);
    }

    private String validatePhoneNumber(final OAuthUserInfo userInfo, final String phoneNumber) {
        if (userInfo.phoneNumber() == null && phoneNumber == null) {
            throw new InvalidRequestException(ErrorCode.INVALID_PHONE_NUMBER);
        }

        if (userInfo.phoneNumber() != null) {
            return toDomesticFormat(userInfo.phoneNumber());
        }
        return validatePhonePattern(phoneNumber);
    }

    private void validateSignUpFields(final String nickName, final UserRole userRole) {
        if (nickName == null || nickName.isBlank()) {
            throw new InvalidRequestException(ErrorCode.INVALID_NICK_NAME);
        }
        if (userQueryPort.existsByNickName(nickName)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME);
        }
        if (userRole == null) {
            throw new InvalidRequestException(ErrorCode.INVALID_USER_ROLE);
        }
    }

    private void validateUserRole(final UserRole userRole) {
        if (!userRole.isUserAssignable()) {
            throw new InvalidRequestException(ErrorCode.USER_ROLE_NOT_ASSIGNABLE);
        }
    }

    private String toDomesticFormat(String phone) {
        String digits = phone.replaceAll("[^0-9]", "");
        if (digits.startsWith("82") && digits.length() > 10) {
            return "0" + digits.substring(2);
        }
        return digits;
    }

    private String validatePhonePattern(final String value) {
        if (!value.matches("^(?!.*-)[0-9]{10,11}$")) {
            throw new InvalidRequestException(ErrorCode.USER_VALID_PHONE);
        }
        return value;
    }
}
