package com.magambell.server.favorite.app.port.in.request;

public record FavoriteStoreListServiceRequest(
        Integer page,
        Integer size,
        Long userId
) {
}
