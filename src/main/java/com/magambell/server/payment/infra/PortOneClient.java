package com.magambell.server.payment.infra;

import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import com.magambell.server.payment.app.port.out.PortOnePort;
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
    private final WebClient webClient;

    @Value("${portone.secret.store-id}")
    private String storeId;

    @Value("${portone.secret.api-key}")
    private String apiKey;

    @Override
    public PortOnePaymentResponse getPaymentById(final String paymentId) {
        return webClient.get()
                .uri("https://api.portone.io/payments/{paymentId}", paymentId)
                .header("Authorization", "Bearer " + apiKey)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> {
                            log.warn("PortOne API 에러 발생: {}", clientResponse.statusCode());
                            return Mono.error(
                                    new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND)
                            );
                        }
                )
                .bodyToMono(PortOnePaymentResponse.class)
                .block();
    }
}
