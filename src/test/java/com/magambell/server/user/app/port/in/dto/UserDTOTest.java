package com.magambell.server.user.app.port.in.dto;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.user.domain.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UserDTO 테스트")
class UserDTOTest {

    @Test
    @DisplayName("이메일 형식이 올바르면 예외가 발생하지 않는다")
    void validEmail() {
        assertThatNoException().isThrownBy(
                () -> new UserDTO("valid@test.com", "Password123!", "홍길동", "01012345678", UserRole.CUSTOMER));
    }

    @Test
    @DisplayName("이메일 형식이 올바르지 않으면 예외가 발생한다")
    void invalidEmail() {
        assertThatThrownBy(() ->
                new UserDTO("invalid-email", "Password123!", "홍길동", "01012345678", UserRole.CUSTOMER)
        ).isInstanceOf(InvalidRequestException.class)
                .hasMessage(ErrorCode.USER_VALID_EMAIL.getMessage());
    }

    @Test
    @DisplayName("전화번호 형식이 올바르지 않으면 예외가 발생한다")
    void invalidPhone() {
        assertThatThrownBy(() ->
                new UserDTO("valid@test.com", "Password123!", "홍길동", "010-1234-5678", UserRole.CUSTOMER)
        ).isInstanceOf(InvalidRequestException.class)
                .hasMessage(ErrorCode.USER_VALID_PHONE.getMessage());
    }
}
