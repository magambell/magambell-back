package com.magambell.server.banner.app.port.out;

import com.magambell.server.banner.adaper.in.web.BannerImagesRegister;
import com.magambell.server.banner.app.port.in.dto.RegisterBannerDTO;
import com.magambell.server.banner.app.port.out.response.BannerRegisterResponseDTO;
import com.magambell.server.banner.app.port.out.response.EditBannerImageResponseDTO;
import com.magambell.server.banner.domain.entity.Banner;

import java.util.List;

public interface BannerCommandPort {

    BannerRegisterResponseDTO registerBanner(RegisterBannerDTO dto);

    List<Banner> getBannerList();

    EditBannerImageResponseDTO editBannerImage(Banner banner, BannerImagesRegister bannerImagesRegister);

    void delete(Banner banner);

}
