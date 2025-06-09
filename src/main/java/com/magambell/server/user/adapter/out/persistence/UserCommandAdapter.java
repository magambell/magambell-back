package com.magambell.server.user.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.user.app.port.in.dto.UserDTO;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import com.magambell.server.user.app.port.out.UserCommandPort;
import com.magambell.server.user.domain.model.User;
import com.magambell.server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class UserCommandAdapter implements UserCommandPort {

    private final UserRepository userRepository;

    @Override
    public User register(final UserDTO dto) {
        return userRepository.save(dto.toUser());
    }

    @Override
    public User registerBySocial(final UserSocialAccountDTO dto) {
        User user = dto.toUser();
        user.addUserSocialAccount(dto.toUserSocialAccount());

        userRepository.save(user);
        return user;
    }
}
