package com.magambell.server.store.app.port.in.request;

import com.magambell.server.store.domain.enums.SearchSortType;

public record SearchStoreListServiceRequest(
        Double latitude,
        Double longitude,
        String keyword,
        SearchSortType sortType,
        Boolean onlyAvailable,
        Integer page,
        Integer size
) {
}
