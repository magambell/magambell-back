package com.magambell.server.store.app.port.in;

import com.magambell.server.store.app.port.in.request.RegisterStoreServiceRequest;

public interface StoreUseCase {
    void registerStore(RegisterStoreServiceRequest request, Long userId);
}
