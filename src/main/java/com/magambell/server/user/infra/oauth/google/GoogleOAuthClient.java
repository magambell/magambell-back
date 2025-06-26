package com.magambell.server.user.infra.oauth.google;

import com.magambell.server.auth.domain.ProviderType;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.user.app.dto.OAuthUserInfo;
import com.magambell.server.user.app.port.out.OAuthClient;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class GoogleOAuthClient implements OAuthClient {

    private final WebClient webClient;

    @Value("${oauth.google.uri}")
    private String GOOGLE_URI;

    @Value("${oauth.google.revoke-uri}")
    private String GOOGLE_REVOKE_URI;

    @Override
    public ProviderType getProviderType() {
        return ProviderType.GOOGLE;
    }

    @Override
    public OAuthUserInfo getUserInfo(final String accessToken) {
        GoogleUserResponse response = fetchGoogleUserResponse(accessToken);
        validateGoogleResponse(response);

        return new OAuthUserInfo(
                response.id(),
                response.email(),
                ProviderType.GOOGLE
        );
    }

    @Override
    public void userWithdraw(final String accessToken) {
        webClient.post()
                .uri(GOOGLE_REVOKE_URI)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .bodyValue("token=" + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(
                                new NotFoundException(ErrorCode.OAUTH_GOOGLE_USER_NOT_FOUND))
                )
                .bodyToMono(Void.class)
                .block();
    }

    @Override
    public Optional<OAuthUserInfo> findUserBySocialId(final String accessToken) {
        GoogleUserResponse response = fetchGoogleUserResponse(accessToken);
        if (response == null) {
            return Optional.empty();
        }
        return Optional.of(
                new OAuthUserInfo(
                        response.id(),
                        response.email(),
                        ProviderType.GOOGLE
                )
        );
    }

    private GoogleUserResponse fetchGoogleUserResponse(String accessToken) {
        return webClient.get()
                .uri(GOOGLE_URI)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(
                                new NotFoundException(ErrorCode.OAUTH_GOOGLE_USER_NOT_FOUND))
                )
                .bodyToMono(GoogleUserResponse.class)
                .block();
    }

    private void validateGoogleResponse(GoogleUserResponse response) {
        if (response == null || response.id() == null || response.email() == null) {
            throw new NotFoundException(ErrorCode.OAUTH_GOOGLE_USER_NOT_FOUND);
        }
    }
}
