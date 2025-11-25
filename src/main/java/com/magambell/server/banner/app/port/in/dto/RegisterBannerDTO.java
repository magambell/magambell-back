package com.magambell.server.banner.app.port.in.dto;

import com.magambell.server.banner.adaper.in.web.BannerImagesRegister;
import com.magambell.server.common.s3.dto.ImageRegister;

public record RegisterBannerDTO(
        BannerImagesRegister bannerImagesRegister
) {
    public ImageRegister toImage() {
        return new ImageRegister(bannerImagesRegister.id(), bannerImagesRegister.key());
    }
}
