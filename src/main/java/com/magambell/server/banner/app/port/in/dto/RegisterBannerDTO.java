package com.magambell.server.banner.app.port.in.dto;

import com.magambell.server.banner.adaper.in.web.BannerImagesRegister;
import com.magambell.server.common.s3.dto.ImageRegister;
import com.magambell.server.store.domain.entity.Store;
import com.magambell.server.user.domain.entity.User;

import java.util.List;

public record RegisterBannerDTO(
        String name,
        Integer order,
        BannerImagesRegister bannerImagesRegister
) {
    public ImageRegister toImage() {
        return new ImageRegister(bannerImagesRegister.id(), bannerImagesRegister.key());
    }
}
