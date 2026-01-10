package com.magambell.server.store.app.port.in.request;

public record StoreSearchServiceRequest(
        String query,
        String sort,
        Integer limit,
        String cursor
) {
}
