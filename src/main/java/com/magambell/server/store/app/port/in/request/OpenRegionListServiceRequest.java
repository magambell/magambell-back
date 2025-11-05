package com.magambell.server.store.app.port.in.request;


public record OpenRegionListServiceRequest(
        Integer page,
        Integer size
) {
}
