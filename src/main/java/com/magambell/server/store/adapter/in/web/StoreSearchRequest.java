package com.magambell.server.store.adapter.in.web;

import com.magambell.server.store.app.port.in.request.StoreSearchServiceRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record StoreSearchRequest(
        String q,
        String sort,
        
        @Min(value = 1, message = "limit은 1 이상이어야 합니다.")
        @Max(value = 100, message = "limit은 100 이하여야 합니다.")
        Integer limit,
        
        String cursor
) {
    public StoreSearchServiceRequest toService() {
        return new StoreSearchServiceRequest(
                q != null ? q : "",
                sort != null ? sort : "-createdAt",
                limit != null ? limit : 20,
                cursor
        );
    }
}
