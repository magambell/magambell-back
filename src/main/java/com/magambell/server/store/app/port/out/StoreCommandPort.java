package com.magambell.server.store.app.port.out;

import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.store.app.port.in.dto.RegisterStoreDTO;
import com.magambell.server.store.app.port.out.response.EditStoreImageResponseDTO;
import com.magambell.server.store.app.port.out.response.StoreRegisterResponseDTO;
import com.magambell.server.store.domain.entity.Store;
import java.util.List;

public interface StoreCommandPort {
    StoreRegisterResponseDTO registerStore(RegisterStoreDTO dto);

    void storeApprove(Long storeId);

    EditStoreImageResponseDTO editStoreImage(Store store, List<StoreImagesRegister> storeImagesRegisters);
}
