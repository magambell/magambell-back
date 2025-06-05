package com.magambell.server.user.app.port.out;

import com.magambell.server.user.app.port.in.dto.UserDTO;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import com.magambell.server.user.domain.model.User;

public interface UserCommandPort {

    User register(UserDTO userDTO);

    User registerBySocial(UserSocialAccountDTO userSocialAccountDTO);

}
