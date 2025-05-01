package com.magambell.server.user.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.common.exception.NotEqualException;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.user.app.port.in.dto.UserEmailDTO;
import com.magambell.server.user.app.port.in.request.RegisterServiceRequest;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.enums.VerificationStatus;
import com.magambell.server.user.domain.model.User;
import com.magambell.server.user.domain.repository.UserEmailRepository;
import com.magambell.server.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEmailRepository userEmailRepository;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAllInBatch();
        userEmailRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입")
    @Test
    void register() {
        // given
        String authCode = "testCode";
        String email = "test@test.com";
        UserEmailDTO userEmailDTO = new UserEmailDTO(email, authCode, VerificationStatus.REGISTER);
        userEmailRepository.save(userEmailDTO.toUserEmail());

        RegisterServiceRequest request = new RegisterServiceRequest(email, "Qwer1234!!", "test", "01012341234",
                UserRole.CUSTOMER, authCode);

        // when
        userService.register(request);

        // then
        User user = userRepository.findAll().get(0);
        assertThat(user).isNotNull();
        assertThat(user).extracting("email", "name", "phoneNumber")
                .contains(
                        "test@test.com",
                        "test",
                        "01012341234"
                );
    }

    @DisplayName("회원가입시 인증번호가 다르면 예외가 발생한다.")
    @Test
    void registerWithNotEqualAuthCode() {
        // given
        String authCode = "testCode";
        String email = "test@test.com";
        UserEmailDTO userEmailDTO = new UserEmailDTO(email, "testCode2", VerificationStatus.REGISTER);
        userEmailRepository.save(userEmailDTO.toUserEmail());

        RegisterServiceRequest request = new RegisterServiceRequest(email, "Qwer1234!!", "test", "01012341234",
                UserRole.CUSTOMER, authCode);

        // when // then
        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(NotEqualException.class)
                .hasMessage(ErrorCode.USER_EMAIL_AUTH_CODE_NOT_EQUALS.getMessage());
    }

    @DisplayName("회원가입시 인증번호가 존재하지 않으면 예외가 발생한다.")
    @Test
    void registerWithNotFoundAuthCode() {
        // given
        String authCode = "testCode";
        String email = "test@test.com";
        RegisterServiceRequest request = new RegisterServiceRequest(email, "Qwer1234!!", "test", "01012341234",
                UserRole.CUSTOMER, authCode);

        // when // then
        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ErrorCode.USER_EMAIL_NOT_FOUND.getMessage());
    }

    @DisplayName("회원가입시 비밀번호에 대문자, 소문자, 숫자, 특수문자 모두 포함되지 않으면 오류가 발생한다.")
    @ValueSource(strings = {
            "qwer1234!!",     // 대문자 없음
            "QWER1234!!",     // 소문자 없음
            "Qwerasdf!!",     // 숫자 없음
            "Qwer1234",       // 특수문자 없음
            "Q1!",            // 길이 부족
            "Qwer1234!!Qwer1234!!" // 길이 초과
    })
    @ParameterizedTest
    void registerWithInvalidRequestPassword(String password) {
        // given
        String authCode = "testCode";
        String email = "test@test.com";
        UserEmailDTO userEmailDTO = new UserEmailDTO(email, authCode, VerificationStatus.REGISTER);
        userEmailRepository.save(userEmailDTO.toUserEmail());

        RegisterServiceRequest request = new RegisterServiceRequest(email, password, "test", "01012341234",
                UserRole.CUSTOMER, authCode);

        // when // then
        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(ErrorCode.USER_VALID_PASSWORD.getMessage());
    }
}