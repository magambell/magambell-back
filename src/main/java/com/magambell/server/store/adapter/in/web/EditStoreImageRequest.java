package com.magambell.server.store.adapter.in.web;

import com.magambell.server.store.app.port.in.request.EditStoreImageServiceRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record EditStoreImageRequest(
        @NotNull(message = "매장을 선택해 주세요.")
        Long storeId,

        @NotEmpty(message = "대표 이미지는 필수입니다.")
        List<StoreImagesRegister> storeImagesRegisters
) {

    public EditStoreImageServiceRequest toService(final Long userId) {
        return new EditStoreImageServiceRequest(userId, storeId, storeImagesRegisters);
    }
}
