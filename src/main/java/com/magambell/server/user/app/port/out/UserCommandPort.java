package com.magambell.server.user.app.port.out;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.user.app.port.in.dto.UserDTO;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import com.magambell.server.user.domain.entity.User;

public interface UserCommandPort {

    User register(UserDTO userDTO);

    User registerBySocial(UserSocialAccountDTO userSocialAccountDTO);

    void deleteBySocial(final ProviderType providerType, final Long userId);
}
