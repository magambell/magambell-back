package com.magambell.server.store.app.port.in.dto;

import com.magambell.server.common.s3.dto.ImageRegister;
import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.store.domain.enums.Approved;
import com.magambell.server.store.domain.enums.Bank;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.user.domain.entity.User;
import java.util.List;

public record RegisterStoreDTO(
        String name,
        String address,
        Double latitude,
        Double longitude,
        String ownerName,
        String ownerPhone,
        String businessNumber,
        Bank bankName,
        String bankAccount,
        List<StoreImagesRegister> storeImagesRegisters,
        Approved approved,
        User user
) {
    public Store toEntity() {
        return Store.create(this);
    }

    public List<ImageRegister> toImage() {
        return storeImagesRegisters.stream()
                .map(image -> new ImageRegister(image.id(), image.key()))
                .toList();
    }
}
