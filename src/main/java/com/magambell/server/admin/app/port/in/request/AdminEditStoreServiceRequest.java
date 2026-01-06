package com.magambell.server.admin.app.port.in.request;

import com.magambell.server.admin.app.port.in.dto.AdminEditStoreDTO;
import com.magambell.server.goods.domain.enums.SaleStatus;
import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;
import com.magambell.server.store.domain.enums.Bank;

import java.time.LocalDateTime;
import java.util.List;

public record AdminEditStoreServiceRequest(
        String storeName,
        String address,
        Double latitude,
        Double longitude,
        String ownerName,
        String ownerPhone,
        String businessNumber,
        Bank bankName,
        String bankAccount,
        String description,
        String parkingDescription,
        List<StoreImagesRegister> storeImages,
        String goodsName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer originalPrice,
        Integer salePrice,
        Integer discount,
        Integer quantity,
        SaleStatus saleStatus,
        List<GoodsImagesRegister> goodsImages
) {
    public AdminEditStoreDTO toDTO() {
        return new AdminEditStoreDTO(
                storeName, address, latitude, longitude,
                ownerName, ownerPhone, businessNumber, bankName, bankAccount,
                description, parkingDescription, storeImages,
                goodsName, startTime, endTime, originalPrice, salePrice, discount,
                quantity, saleStatus, goodsImages
        );
    }
}
