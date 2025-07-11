package com.magambell.server.notification.app.port.in.request;

import com.magambell.server.store.domain.model.Store;

public record NotifyStoreOpenRequest(
        Store store
) {
}
