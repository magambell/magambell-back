package com.magambell.server.store.adapter.in.web;

import com.magambell.server.store.app.port.in.request.OpenRegionListServiceRequest;
import com.magambell.server.store.app.port.in.request.SearchStoreListServiceRequest;
import com.magambell.server.store.domain.enums.SearchSortType;
import jakarta.validation.constraints.Positive;

public record OpenRegionListRequest(

        @Positive(message = "페이지를 선택해 주세요.")
        Integer page,

        @Positive(message = "화면에 개수를 주세요.")
        Integer size
) {
    public OpenRegionListServiceRequest toService() {
        return new OpenRegionListServiceRequest(page, size);
    }
}
