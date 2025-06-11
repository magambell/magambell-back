package com.magambell.server.store.adapter.in.web;

import com.magambell.server.store.app.port.in.request.RegisterStoreServiceRequest;
import com.magambell.server.store.domain.enums.Bank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RegisterStoreRequest(
        @NotBlank(message = "이름을 입력해 주세요.")
        String name,

        @NotBlank(message = "주소을 입력해 주세요.")
        String address,

        @NotBlank(message = "위도를 입력해 주세요.")
        String latitude,

        @NotBlank(message = "경도를 입력해 주세요.")
        String longitude,

        @NotBlank(message = "대표님 성명을 입력해 주세요.")
        String ownerName,

        @NotBlank(message = "대표님 번호를 입력해 주세요.")
        String ownerPhone,

        @NotBlank(message = "사업자 등록 번호를 입력해 주세요.")
        String businessNumber,

        @NotNull(message = "은행을 선택해 주세요.")
        Bank bankName,

        @NotBlank(message = "계좌번호를 입력해 주세요.")
        String bankAccount,

        @NotEmpty(message = "대표 이미지는 필수입니다.")
        List<StoreImagesRegister> storeImagesRegisters
) {
    public RegisterStoreServiceRequest toServiceRequest() {
        return new RegisterStoreServiceRequest(
                name,
                address,
                latitude,
                longitude,
                ownerName,
                ownerPhone,
                businessNumber,
                bankName,
                bankAccount,
                storeImagesRegisters);
    }
}
