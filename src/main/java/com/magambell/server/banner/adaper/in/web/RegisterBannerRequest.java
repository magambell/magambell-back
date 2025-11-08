package com.magambell.server.banner.adaper.in.web;

import com.magambell.server.banner.app.port.in.request.RegisterBannerServiceRequest;
import jakarta.validation.constraints.NotNull;

public record RegisterBannerRequest(

        @NotNull(message = "배너 이미지는 필수입니다.")
        BannerImagesRegister bannerImagesRegister
) {
    public RegisterBannerServiceRequest toServiceRequest() {
        return new RegisterBannerServiceRequest(
                bannerImagesRegister
        );
    }
}
