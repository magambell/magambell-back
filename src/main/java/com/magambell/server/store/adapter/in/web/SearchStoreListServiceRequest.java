package com.magambell.server.store.adapter.in.web;

import com.magambell.server.store.domain.enums.SearchSortType;

public record SearchStoreListServiceRequest(
        Double latitude,
        Double longitude,
        String keyword,
        SearchSortType sortType,
        Boolean onlyAvailable
) {
}
