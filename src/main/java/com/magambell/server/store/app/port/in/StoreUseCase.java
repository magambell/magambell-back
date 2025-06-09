package com.magambell.server.store.app.port.in;

import com.magambell.server.store.adapter.out.persistence.StoreImagesResponse;
import com.magambell.server.store.app.port.in.request.RegisterStoreServiceRequest;

public interface StoreUseCase {
    StoreImagesResponse registerStore(RegisterStoreServiceRequest request, Long userId);
}
