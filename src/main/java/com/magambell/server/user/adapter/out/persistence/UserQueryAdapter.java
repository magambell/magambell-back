package com.magambell.server.user.adapter.out.persistence;

import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.user.app.port.in.dto.UserDTO;
import com.magambell.server.user.app.port.out.UserQueryPort;
import com.magambell.server.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Adapter
public class UserQueryAdapter implements UserQueryPort {

    private final UserRepository userRepository;

    @Override
    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void register(final UserDTO userDTO) {
        userRepository.save(userDTO.toUser());
    }
}
