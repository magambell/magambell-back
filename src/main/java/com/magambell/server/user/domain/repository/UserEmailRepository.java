package com.magambell.server.user.domain.repository;

import com.magambell.server.user.app.port.in.dto.UserEmailDTO;
import com.magambell.server.user.domain.enums.VerificationStatus;
import com.magambell.server.user.domain.model.UserEmail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEmailRepository extends JpaRepository<UserEmail, Long> {

    void deleteByEmail(String email);

    Optional<UserEmailDTO> findByEmailAndVerificationStatus(String email, VerificationStatus verificationStatus);

    boolean existsByEmailAndAuthCodeAndVerificationStatus(String email, String authCode,
                                                          VerificationStatus verificationStatus);
}
