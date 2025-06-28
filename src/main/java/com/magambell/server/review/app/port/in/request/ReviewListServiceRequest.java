package com.magambell.server.review.app.port.in.request;

public record ReviewListServiceRequest(
        Long goodsId,
        Boolean imageCheck
) {
}
