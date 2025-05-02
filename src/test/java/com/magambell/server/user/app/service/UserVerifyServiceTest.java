package com.magambell.server.user.app.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.DuplicateException;
import com.magambell.server.common.exception.NotEqualException;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.user.app.port.in.dto.UserDTO;
import com.magambell.server.user.app.port.in.dto.UserEmailDTO;
import com.magambell.server.user.app.port.in.request.VerifyEmailAuthCodeServiceRequest;
import com.magambell.server.user.app.port.in.request.VerifyEmailServiceRequest;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.enums.VerificationStatus;
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
    void emailRegisterDuplicate() {
        // given
        VerifyEmailServiceRequest request = new VerifyEmailServiceRequest("test@test.com");

        // when // then
        assertDoesNotThrow(() -> userVerifyService.emailRegisterDuplicate(request));
    }

    @DisplayName("회원가입시 이메일이 중복일때 오류가 발생한다.")
    @Test
    void emailRegisterDuplicateWithException() {
        // given
        String email = "test@test.com";
        UserDTO userDTO = new UserDTO(email, "qwer1234", "test", "01012341234", UserRole.CUSTOMER);
        userRepository.save(userDTO.toUser());

        VerifyEmailServiceRequest request = new VerifyEmailServiceRequest(email);

        // when // then
        assertThatThrownBy(() -> userVerifyService.emailRegisterDuplicate(request))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(ErrorCode.DUPLICATE_EMAIL.getMessage());
    }

    @DisplayName("회원가입시 이메일과 인증번호가 맞으면 성공한다.")
    @Test
    void emailRegisterAuthCodeCheck() {
        // given
        String authCode = "testCode";
        String email = "test@test.com";
        UserEmailDTO userEmailDTO = new UserEmailDTO(email, authCode, VerificationStatus.REGISTER);
        userEmailRepository.save(userEmailDTO.toUserEmail());

        VerifyEmailAuthCodeServiceRequest request = new VerifyEmailAuthCodeServiceRequest(email, authCode);

        // when // then
        assertDoesNotThrow(() -> userVerifyService.emailRegisterAuthCodeCheck(request));
    }

    @DisplayName("회원가입시 인증번호가 다르면 예외가 발생한다.")
    @Test
    void registerWithNotEqualAuthCode() {
        // given
        String authCode = "testCode";
        String email = "test@test.com";
        UserEmailDTO userEmailDTO = new UserEmailDTO(email, "testCode2", VerificationStatus.REGISTER);
        userEmailRepository.save(userEmailDTO.toUserEmail());

        VerifyEmailAuthCodeServiceRequest request = new VerifyEmailAuthCodeServiceRequest(email, authCode);

        // when // then
        assertThatThrownBy(() -> userVerifyService.emailRegisterAuthCodeCheck(request))
                .isInstanceOf(NotEqualException.class)
                .hasMessage(ErrorCode.USER_EMAIL_AUTH_CODE_NOT_EQUALS.getMessage());
    }

    @DisplayName("회원가입시 인증번호가 존재하지 않으면 예외가 발생한다.")
    @Test
    void emailRegisterWithNotFoundAuthCode() {
        // given
        String authCode = "testCode";
        String email = "test@test.com";
        VerifyEmailAuthCodeServiceRequest request = new VerifyEmailAuthCodeServiceRequest(email, authCode);

        // when // then
        assertThatThrownBy(() -> userVerifyService.emailRegisterAuthCodeCheck(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage());
    }
}