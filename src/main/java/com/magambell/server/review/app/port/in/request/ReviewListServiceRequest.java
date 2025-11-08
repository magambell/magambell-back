package com.magambell.server.review.app.port.in.request;

public record ReviewListServiceRequest(
        Long userId,
        Long goodsId,
        Boolean imageCheck,
        Integer page,
        Integer size
) {
}
