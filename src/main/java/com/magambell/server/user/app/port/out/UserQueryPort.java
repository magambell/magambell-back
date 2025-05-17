package com.magambell.server.user.app.port.out;

import com.magambell.server.user.app.port.in.dto.UserDTO;
import com.magambell.server.user.domain.model.User;

public interface UserQueryPort {

    boolean existsByEmail(String email);

    void register(UserDTO userDTO);

    User getUser(String email, String password);
}
