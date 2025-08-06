package com.magambell.server.review.app.port.in.request;

public record ReviewRatingAllServiceRequest(
        Long goodsId,
        Boolean imageCheck
) {
}
