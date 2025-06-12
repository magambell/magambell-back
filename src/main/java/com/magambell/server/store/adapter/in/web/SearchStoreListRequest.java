package com.magambell.server.store.adapter.in.web;

import com.magambell.server.store.domain.enums.SearchSortType;

public record SearchStoreListRequest(
        Double latitude,
        Double longitude,
        String keyword,
        SearchSortType sortType,
        Boolean onlyAvailable
) {
    public SearchStoreListServiceRequest toService() {
        return new SearchStoreListServiceRequest(latitude, longitude, keyword, sortType, onlyAvailable);
    }
}
