package com.magambell.server.banner.adaper.in.web;

import com.magambell.server.banner.app.port.in.request.RegisterBannerServiceRequest;
import com.magambell.server.store.adapter.in.web.StoreImagesRegister;
import com.magambell.server.store.app.port.in.request.RegisterStoreServiceRequest;
import com.magambell.server.store.domain.enums.Bank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RegisterBannerRequest(

        @NotBlank(message = "배너 이름을 입력해 주세요.")
        String name,

        @NotNull(message = "배너 순서를 입력해 주세요.")
        Integer order,

        @NotNull(message = "배너 이미지는 필수입니다.")
        BannerImagesRegister bannerImagesRegister
) {
    public RegisterBannerServiceRequest toServiceRequest() {
        return new RegisterBannerServiceRequest(
                name,
                order,
                bannerImagesRegister
        );
    }
}
