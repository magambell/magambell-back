package com.magambell.server.banner.adaper.in.web;

import com.magambell.server.banner.app.port.in.request.EditBannerImageServiceRequest;
import jakarta.validation.constraints.NotNull;

public record EditBannerImageRequest(
        @NotNull(message = "배너를 선택해 주세요.")
        Long bannerId,

        @NotNull(message = "배너 이미지는 필수입니다.")
        BannerImagesRegister bannerImagesRegister
) {

    public EditBannerImageServiceRequest toService() {
        return new EditBannerImageServiceRequest(bannerId, bannerImagesRegister);
    }
}
