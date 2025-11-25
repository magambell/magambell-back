package com.magambell.server.banner.app.port.in.request;

import com.magambell.server.banner.adaper.in.web.BannerImagesRegister;
import com.magambell.server.banner.app.port.in.dto.RegisterBannerDTO;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;

public record RegisterBannerServiceRequest(
        BannerImagesRegister bannerImagesRegister
) {
    public RegisterBannerServiceRequest(final BannerImagesRegister bannerImagesRegister) {
        this.bannerImagesRegister = validateImages(bannerImagesRegister);
    }

    private BannerImagesRegister validateImages(final BannerImagesRegister value) {
        if (value == null) {
            throw new InvalidRequestException(ErrorCode.BANNER_VALID_IMAGE);
        }
        return value;
    }

    public RegisterBannerDTO toBannerDTO() {
        return new RegisterBannerDTO(bannerImagesRegister);
    }
}
