package com.magambell.server.banner.app.port.in.request;

import com.magambell.server.banner.adaper.in.web.BannerImagesRegister;

public record EditBannerImageServiceRequest(Long bannerId,
                                            BannerImagesRegister bannerImagesRegister) {
}
