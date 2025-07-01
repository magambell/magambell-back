package com.magambell.server.order.app.port.in.request;

public record OwnerOrderListServiceRequest(
        Integer page,
        Integer size
) {
}
