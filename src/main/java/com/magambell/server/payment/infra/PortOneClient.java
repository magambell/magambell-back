package com.magambell.server.payment.infra;

import com.magambell.server.payment.app.port.out.PortOnePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Component
public class PortOneClient implements PortOnePort {
    private final WebClient webClient;

    private String apiKey;

    private String portOneApiUrl;


}
