package com.magambell.server.favorite.adapter.in.web;

import com.magambell.server.favorite.app.port.in.request.FavoriteStoreListServiceRequest;
import jakarta.validation.constraints.Positive;

public record FavoriteStoreListRequest(
        @Positive(message = "페이지를 선택해 주세요.")
        Integer page,
        @Positive(message = "화면에 개수를 주세요.")
        Integer size
) {
    public FavoriteStoreListServiceRequest toService(final Long userId) {
        return new FavoriteStoreListServiceRequest(page, size, userId);
    }
}
