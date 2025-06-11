package com.magambell.server.auth.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.magambell.server.auth.app.port.in.request.SocialLoginServiceRequest;
import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.auth.domain.model.JwtToken;
import com.magambell.server.user.app.dto.OAuthUserInfo;
import com.magambell.server.user.app.port.in.dto.UserSocialAccountDTO;
import com.magambell.server.user.app.port.out.OAuthClient;
import com.magambell.server.user.domain.enums.UserRole;
import com.magambell.server.user.domain.model.User;
import com.magambell.server.user.domain.repository.UserRepository;
import com.magambell.server.user.domain.repository.UserSocialAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AuthServiceTest {

    @MockBean
    private OAuthClient oAuthClient;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSocialAccountRepository userSocialAccountRepository;
    @Autowired
    private JwtService jwtService;

    @DisplayName("계정이 없으면 회원가입 처리한다.")
    @Test
    void kakaoSignUp() {
        // given
        SocialLoginServiceRequest socialLoginServiceRequest = new SocialLoginServiceRequest(ProviderType.KAKAO, "test",
                "닉네임", "01012341234", UserRole.CUSTOMER);
        OAuthUserInfo userInfo = new OAuthUserInfo("testId", "test@test.com", "테스트이름", "01012341234",
                ProviderType.KAKAO);

        // when
        when(oAuthClient.getUserInfo(anyString())).thenReturn(userInfo);
        authService.loginOrSignUp(socialLoginServiceRequest);

        // then
        User user = userRepository.findAll().get(0);
        assertThat(user).extracting("email", "name")
                .contains(
                        "test@test.com",
                        "테스트이름");
    }

    @DisplayName("이미 계정이 있으면 로그인 처리한다.")
    @Test
    void kakaoLogin() {
        // given
        UserSocialAccountDTO userSocialAccountDTO = new UserSocialAccountDTO("test@test.com", "테스트이름", "닉네임",
                "01012341234",
                ProviderType.KAKAO,
                "testId", UserRole.CUSTOMER);
        userRepository.save(userSocialAccountDTO.toUser());
        userSocialAccountRepository.save(userSocialAccountDTO.toUserSocialAccount());

        SocialLoginServiceRequest socialLoginServiceRequest = new SocialLoginServiceRequest(ProviderType.KAKAO, "test",
                "닉네임", "01012341234", UserRole.CUSTOMER);
        OAuthUserInfo userInfo = new OAuthUserInfo("testId", "test@test.com", "테스트이름", "01012341234",
                ProviderType.KAKAO);

        // when
        when(oAuthClient.getUserInfo(anyString())).thenReturn(userInfo);
        JwtToken jwtToken = authService.loginOrSignUp(socialLoginServiceRequest);

        // then
        Long userId = jwtService.getJwtUserId(jwtToken.accessToken());
        User user = userRepository.findUserBySocial(ProviderType.KAKAO, "testId")
                .orElse(null);

        assertThat(user).extracting("email", "name")
                .contains(
                        "test@test.com",
                        "테스트이름");
        assertThat(user.getUserSocialAccounts().get(0)).extracting("providerType", "providerId")
                .contains(
                        ProviderType.KAKAO,
                        "testId"
                );
    }
}