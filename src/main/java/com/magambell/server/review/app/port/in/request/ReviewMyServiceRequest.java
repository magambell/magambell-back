package com.magambell.server.review.app.port.in.request;

public record ReviewMyServiceRequest(
        Integer page,
        Integer size,
        Long userId
) {
}
