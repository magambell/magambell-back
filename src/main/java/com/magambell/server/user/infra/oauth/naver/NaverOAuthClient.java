package com.magambell.server.user.infra.oauth.naver;

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
public class NaverOAuthClient implements OAuthClient {

    private final WebClient webClient;

    @Value("${oauth.naver-uri}")
    private String NAVER_URI;

    @Override
    public ProviderType getProviderType() {
        return ProviderType.NAVER;
    }

    @Override
    public OAuthUserInfo getUserInfo(final String accessToken) {
        NaverUserResponse response = fetchNaverUserResponse(accessToken);
        validateNaverResponse(response);

        NaverUserResponse.Response user = response.response();

        return new OAuthUserInfo(
                user.id(),
                user.email(),
                ProviderType.NAVER
        );
    }

    private NaverUserResponse fetchNaverUserResponse(String accessToken) {
        return webClient.get()
                .uri(NAVER_URI)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(
                                new NotFoundException(ErrorCode.OAUTH_NAVER_USER_NOT_FOUND))
                )
                .bodyToMono(NaverUserResponse.class)
                .block();
    }

    private void validateNaverResponse(NaverUserResponse response) {
        if (response == null || response.response() == null || response.response().id() == null) {
            throw new NotFoundException(ErrorCode.OAUTH_NAVER_USER_NOT_FOUND);
        }
    }
}
