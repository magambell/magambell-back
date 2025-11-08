package com.magambell.server.banner.app.service;

import com.magambell.server.banner.adaper.in.web.BannerImagesRegister;
import com.magambell.server.banner.adaper.out.persistence.BannerImageResponse;
import com.magambell.server.banner.adaper.out.persistence.BannerImagesResponse;
import com.magambell.server.banner.app.port.in.BannerUseCase;
import com.magambell.server.banner.app.port.in.request.DeleteBannerServiceRequest;
import com.magambell.server.banner.app.port.in.request.EditBannerImageServiceRequest;
import com.magambell.server.banner.app.port.in.request.RegisterBannerServiceRequest;
import com.magambell.server.banner.app.port.out.BannerCommandPort;
import com.magambell.server.banner.app.port.out.BannerQueryPort;
import com.magambell.server.banner.app.port.out.response.BannerPreSignedUrlImage;
import com.magambell.server.banner.app.port.out.response.BannerRegisterResponseDTO;
import com.magambell.server.banner.app.port.out.response.EditBannerImageResponseDTO;
import com.magambell.server.banner.domain.entity.Banner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BannerService implements BannerUseCase {

    private final BannerCommandPort bannerCommandPort;
    private final BannerQueryPort bannerQueryPort;
//    private final UserQueryPort userQueryPort;


    @Transactional
    @Override
    public BannerImageResponse registerBanner(final RegisterBannerServiceRequest request) {
        BannerRegisterResponseDTO bannerRegisterResponseDTO = bannerCommandPort.registerBanner(
                request.toBannerDTO());

        return new BannerImageResponse(String.valueOf(bannerRegisterResponseDTO.id()),
                bannerRegisterResponseDTO.bannerPreSignedUrlImage());
    }

    @Override
    public BannerImagesResponse getBannerImageList() {

        List<BannerPreSignedUrlImage> images =
                Optional.ofNullable(bannerQueryPort.getBannerList())
                        .orElseGet(List::of).stream()
                        .filter(Objects::nonNull)
                        .map(banner -> new BannerPreSignedUrlImage(banner.getId(), banner.getOrder(), banner.getUrl()))
                        .toList();

        return new BannerImagesResponse(images);
    }

    @Override
    @Transactional
    public BannerImageResponse editBannerImage(final EditBannerImageServiceRequest request) {
        Banner banner = bannerQueryPort.findById(request.bannerId());
        banner.modifyOrder(request.bannerImagesRegister().id());

        EditBannerImageResponseDTO editBannerImageResponseDTO = changeBannerImage(banner, request.bannerImagesRegister());

        return new BannerImageResponse(String.valueOf(editBannerImageResponseDTO.id()),
                editBannerImageResponseDTO.bannerPreSignedUrlImage());
    }

    @Override
    @Transactional
    public void deleteBanner(final DeleteBannerServiceRequest request) {
        Banner banner = bannerQueryPort.findById(request.bannerId());
        bannerCommandPort.delete(banner);
    }


    private EditBannerImageResponseDTO changeBannerImage(final Banner banner,
                                                         final BannerImagesRegister bannerImagesRegister) {
        return bannerCommandPort.editBannerImage(banner, bannerImagesRegister);
    }
}
