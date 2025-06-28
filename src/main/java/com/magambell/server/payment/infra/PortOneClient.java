package com.magambell.server.payment.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.payment.app.port.out.PortOnePort;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class PortOneClient implements PortOnePort {
    private static final String BASE_URL = "https://api.portone.io";

    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    @Value("${portone.secret.store-id}")
    private String storeId;

    @Value("${portone.secret.api-key}")
    private String apiSecret;

    @Override
    public PortOnePaymentResponse getPaymentById(final String paymentId) {
        String accessToken = getPortOneAccessToken();
        return webClient.get()
                .uri(BASE_URL + "/payments/{paymentId}", paymentId)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.warn("PortOne API 에러 발생: {} | Body: {}", clientResponse.statusCode(),
                                            errorBody);
                                    return Mono.error(new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND));
                                })
                )
                .bodyToMono(String.class)
                .doOnNext(raw -> log.info("🧾 PortOne 원본 응답: {}", raw))
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, PortOnePaymentResponse.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .block();
    }

    private String getPortOneAccessToken() {
        String tokenUri = BASE_URL + "/login/api-secret";

        Map<String, String> body = Map.of("apiSecret", apiSecret);

        PortOneTokenResponse response = webClient.post()
                .uri(tokenUri)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(PortOneTokenResponse.class)
                .block();

        if (response == null || response.accessToken() == null) {
            log.error("PortOne AccessToken 발급 실패: 응답이 null이거나 accessToken이 없음.");
            throw new InvalidRequestException(ErrorCode.INVALID_PORT_ONE_ACCESS_TOKEN);
        }

        return response.accessToken();
    }
}
