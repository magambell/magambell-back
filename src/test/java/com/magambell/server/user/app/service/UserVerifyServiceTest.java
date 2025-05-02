package com.magambell.server.user.app.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.user.app.port.in.dto.UserDTO;
import com.magambell.server.user.app.port.in.request.VerifyEmailServiceRequest;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.repository.UserEmailRepository;
import com.magambell.server.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserVerifyServiceTest {

    @Autowired
    private UserVerifyService userVerifyService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEmailRepository userEmailRepository;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAllInBatch();
        userEmailRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입시 이메일이 중복이 아닐시 성공한다.")
    @Test
    void emailSignupDuplicate() {
        // given
        VerifyEmailServiceRequest request = new VerifyEmailServiceRequest("test@test.com");

        // when // then
        assertDoesNotThrow(() -> userVerifyService.emailSignupDuplicate(request));
    }

    @DisplayName("회원가입시 이메일이 중복일때 오류가 발생한다.")
    @Test
    void emailSignupDuplicateWithException() {
        // given
        String email = "test@test.com";
        UserDTO userDTO = new UserDTO(email, "qwer1234", "test", "01012341234", UserRole.CUSTOMER);
        userRepository.save(userDTO.toUser());

        VerifyEmailServiceRequest request = new VerifyEmailServiceRequest(email);

        // when // then
        assertThatThrownBy(() -> userVerifyService.emailSignupDuplicate(request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(ErrorCode.DUPLICATE_EMAIL.getMessage());
    }
}