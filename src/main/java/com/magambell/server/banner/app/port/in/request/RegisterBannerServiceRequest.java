package com.magambell.server.banner.app.port.in.request;

import com.magambell.server.banner.adaper.in.web.BannerImagesRegister;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.InvalidRequestException;
import com.magambell.server.banner.app.port.in.dto.RegisterBannerDTO;


import java.util.List;

public record RegisterBannerServiceRequest(
        String name,
        Integer order,
        BannerImagesRegister bannerImagesRegister
) {
    public RegisterBannerServiceRequest(final String name,
                                        final Integer order,
                                        final BannerImagesRegister bannerImagesRegister) {
        this.name = name;
        this.order = order;
        this.bannerImagesRegister = validateImages(bannerImagesRegister);
    }

    private BannerImagesRegister validateImages(final BannerImagesRegister value) {
        if (value == null) {
            throw new InvalidRequestException(ErrorCode.BANNER_VALID_IMAGE);
        }
        return value;
    }

    public RegisterBannerDTO toBannerDTO() {
        return new RegisterBannerDTO(name, order, bannerImagesRegister);
    }
}
