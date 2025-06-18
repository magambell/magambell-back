package com.magambell.server.store.adapter.in.web;

import com.magambell.server.store.app.port.in.request.StoreApproveServiceRequest;

public record StoreApproveRequest(
        Long id
) {


    public StoreApproveServiceRequest toService() {
        return new StoreApproveServiceRequest(id);
    }
}
