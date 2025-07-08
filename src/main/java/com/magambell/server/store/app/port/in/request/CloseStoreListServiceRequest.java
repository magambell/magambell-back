package com.magambell.server.store.app.port.in.request;

public record CloseStoreListServiceRequest(
        Double latitude,
        Double longitude
) {
}
