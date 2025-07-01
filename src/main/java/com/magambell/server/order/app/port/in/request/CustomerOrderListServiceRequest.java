package com.magambell.server.order.app.port.in.request;

public record CustomerOrderListServiceRequest(
        Integer page,
        Integer size
) {
}
