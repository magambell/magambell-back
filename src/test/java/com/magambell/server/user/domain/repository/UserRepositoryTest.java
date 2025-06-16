package com.magambell.server.user.domain.repository;

import static com.magambell.server.user.domain.enums.UserStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

import com.magambell.server.user.app.port.in.dto.UserDTO;
import com.magambell.server.user.domain.enums.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("이메일로 일반 회원이 존재할때")
    @Test
    void existsByEmailAndUserStatus() {

        // given
        UserDTO userDTO = new UserDTO("test@test.com",
                bCryptPasswordEncoder.encode("password"),
                "test",
                "01012341234",
                UserRole.CUSTOMER);
        userRepository.save(userDTO.toUser());

        // when
        Boolean existsCheck = userRepository.existsByEmailAndUserStatus("test@test.com", ACTIVE);

        // then
        assertThat(existsCheck).isTrue();
    }

    @DisplayName("이메일로 일반 회원이 존재하지 않을때")
    @Test
    void notExistsByEmailAndUserStatus() {

        // given / when
        Boolean existsCheck = userRepository.existsByEmailAndUserStatus("test@test.com", ACTIVE);

        // then
        assertThat(existsCheck).isFalse();
    }

}