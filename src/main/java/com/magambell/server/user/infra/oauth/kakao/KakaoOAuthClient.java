package com.magambell.server.user.infra.oauth.kakao;

import static com.magambell.server.auth.app.service.JwtTokenProvider.ACCESS_PREFIX_STRING;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.user.app.dto.OAuthUserInfo;
import com.magambell.server.user.app.port.out.OAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class KakaoOAuthClient implements OAuthClient {

    private final WebClient webClient;

    @Value("${oauth.kakao.uri}")
    private String KAKAO_URI;

    @Override
    public ProviderType getProviderType() {
        return ProviderType.KAKAO;
    }

    @Override
    public OAuthUserInfo getUserInfo(final String accessToken) {
        KakaoUserResponse response = fetchKakaoUserResponse(accessToken);

        validateKakaoResponse(response);

        return new OAuthUserInfo(
                String.valueOf(response.id()),
                response.kakaoAccount().email(),
                ProviderType.KAKAO
        );
    }

    @Override
    public void userWithdraw(final String accessToken) {
        webClient.post()
                .uri(KAKAO_URI + "/v1/user/unlink")
                .header(HttpHeaders.AUTHORIZATION, ACCESS_PREFIX_STRING + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(
                                new NotFoundException(ErrorCode.OAUTH_KAKAO_USER_NOT_FOUND))
                )
                .bodyToMono(Void.class)
                .block();
    }

    private KakaoUserResponse fetchKakaoUserResponse(String accessToken) {
        return webClient.get()
                .uri(KAKAO_URI + "/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, ACCESS_PREFIX_STRING + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(
                                new NotFoundException(ErrorCode.OAUTH_KAKAO_USER_NOT_FOUND))
                )
                .bodyToMono(KakaoUserResponse.class)
                .block();
    }

    private void validateKakaoResponse(KakaoUserResponse response) {
        if (response == null) {
            throw new NotFoundException(ErrorCode.OAUTH_KAKAO_USER_NOT_FOUND);
        }
        if (response.id() == null) {
            throw new NotFoundException(ErrorCode.OAUTH_KAKAO_USER_NOT_FOUND);
        }
        if (response.kakaoAccount() == null || response.kakaoAccount().email() == null) {
            throw new NotFoundException(ErrorCode.OAUTH_KAKAO_USER_NOT_FOUND);
        }
    }
}
