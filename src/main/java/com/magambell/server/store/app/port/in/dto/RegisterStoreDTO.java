package com.magambell.server.store.app.port.in.dto;

import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.store.domain.enums.Bank;
import com.magambell.server.store.domain.model.Store;
import java.util.List;

public record RegisterStoreDTO(
        String name,
        String address,
        String latitude,
        String longitude,
        String ownerName,
        String ownerPhone,
        String businessNumber,
        Bank bankName,
        String bankAccount,
        List<StoreImagesRegister> storeImagesRegisters,
        Approved approved
) {
    public Store toEntity() {
        return Store.create(this);
    }
}
