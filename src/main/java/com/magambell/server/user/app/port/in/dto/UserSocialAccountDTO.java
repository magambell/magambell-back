package com.magambell.server.user.app.port.in.dto;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.model.User;
import com.magambell.server.user.domain.model.UserSocialAccount;

public record UserSocialAccountDTO(
        String email, String name, ProviderType providerType, String providerId, UserRole userRole
) {

    public UserSocialAccountDTO(final String email, final String name, final ProviderType providerType,
                                final String providerId,
                                final UserRole userRole) {
        this.email = validateEmail(email);
        this.name = name;
        this.providerType = providerType;
        this.providerId = providerId;
        this.userRole = userRole;
    }

    private String validateEmail(final String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidRequestException(ErrorCode.USER_VALID_EMAIL);
        }
        return email;
    }

    public User toUser() {
        return User.createBySocial(this);
    }

    public UserSocialAccount toUserSocialAccount() {
        return UserSocialAccount.create(this);
    }
}
