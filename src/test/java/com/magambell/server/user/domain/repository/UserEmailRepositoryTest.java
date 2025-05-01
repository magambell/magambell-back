package com.magambell.server.user.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserEmailRepositoryTest {

    @Autowired
    private UserEmailRepository userEmailRepository;

    @DisplayName("회원가입시 인증번호가 존재하는지 체크한다.")
    @Test
    void test() {
        // given
//        User saveUser = UserEmail.builder()
//                .email("test@test.com")
////                .password(bCryptPasswordEncoder.encode("password"))
//                .name("Test")
//                .phoneNumber("01012341234")
//                .role(UserRole.CUSTOMER)
//                .build();
        // when

        // then
    }
}