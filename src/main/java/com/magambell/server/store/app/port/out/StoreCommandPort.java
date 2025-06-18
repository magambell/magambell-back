package com.magambell.server.store.app.port.out;

import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.app.port.out.response.StoreRegisterResponseDTO;

public interface StoreCommandPort {
    StoreRegisterResponseDTO registerStore(RegisterStoreDTO dto);

    void storeApprove(Long storeId);
}
