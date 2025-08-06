package com.magambell.server.store.adapter.in.web;

import com.magambell.server.store.app.port.in.request.SearchStoreListServiceRequest;
import com.magambell.server.store.domain.enums.SearchSortType;
import jakarta.validation.constraints.Positive;

public record SearchStoreListRequest(
        Double latitude,
        Double longitude,
        String keyword,
        SearchSortType sortType,
        Boolean onlyAvailable,

        @Positive(message = "페이지를 선택해 주세요.")
        Integer page,

        @Positive(message = "화면에 개수를 주세요.")
        Integer size
) {
    public SearchStoreListServiceRequest toService() {
        return new SearchStoreListServiceRequest(latitude, longitude, keyword, sortType, onlyAvailable, page, size);
    }
}
