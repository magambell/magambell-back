package com.magambell.server.store.app.port.in.request;

import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import java.util.List;

public record EditStoreImageServiceRequest(Long userId, Long storeId,
                                           List<StoreImagesRegister> storeImagesRegisters) {
}
