package com.magambell.server.user.app.port.out;

import com.magambell.server.user.app.port.in.dto.UserEmailDTO;
import com.magambell.server.user.domain.enums.VerificationStatus;
import java.util.Optional;

public interface UserEmailQueryPort {

    void deleteEmail(String email);

    Optional<UserEmailDTO> findRegisterUserEmail(String email, VerificationStatus verificationStatus);

    Long saveUserEmail(String email, String authCode, VerificationStatus verificationStatus);
}
