package com.magambell.server.user.app.port.out;

import com.magambell.server.user.app.port.in.dto.UserDTO;

public interface UserQueryPort {

    boolean existsByEmail(String email);

    void register(UserDTO userDTO);
}
