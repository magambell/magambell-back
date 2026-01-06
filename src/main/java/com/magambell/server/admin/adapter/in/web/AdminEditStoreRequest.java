package com.magambell.server.admin.adapter.in.web;

import com.magambell.server.admin.app.port.in.request.AdminEditStoreServiceRequest;
import com.magambell.server.goods.domain.enums.SaleStatus;
import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.goods.adapter.in.web.GoodsImagesRegister;
import com.magambell.server.store.domain.enums.Bank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record AdminEditStoreRequest(
        @NotBlank String storeName,
        @NotBlank String address,
        @NotNull Double latitude,
        @NotNull Double longitude,
        @NotBlank String ownerName,
        @NotBlank String ownerPhone,
        @NotBlank String businessNumber,
        @NotNull Bank bankName,
        @NotBlank String bankAccount,
        String description,
        String parkingDescription,
        List<StoreImagesRegister> storeImages,
        @NotBlank String goodsName,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotNull Integer originalPrice,
        @NotNull Integer salePrice,
        @NotNull Integer discount,
        @NotNull Integer quantity,
        @NotNull SaleStatus saleStatus,
        List<GoodsImagesRegister> goodsImages
) {
    public AdminEditStoreServiceRequest toServiceRequest() {
        return new AdminEditStoreServiceRequest(
                storeName, address, latitude, longitude,
                ownerName, ownerPhone, businessNumber, bankName, bankAccount,
                description, parkingDescription, storeImages,
                goodsName, startTime, endTime, originalPrice, salePrice, discount,
                quantity, saleStatus, goodsImages
        );
    }
}
