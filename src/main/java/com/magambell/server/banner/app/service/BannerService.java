package com.magambell.server.banner.app.service;

import com.magambell.server.banner.adaper.out.persistence.BannerImageResponse;
import com.magambell.server.banner.adaper.out.persistence.BannerImagesResponse;
import com.magambell.server.banner.app.port.in.BannerUseCase;
import com.magambell.server.banner.app.port.in.request.RegisterBannerServiceRequest;
import com.magambell.server.banner.app.port.out.BannerCommandPort;
import com.magambell.server.banner.app.port.out.BannerQueryPort;
import com.magambell.server.banner.app.port.out.response.BannerPreSignedUrlImage;
import com.magambell.server.banner.app.port.out.response.BannerRegisterResponseDTO;
import com.magambell.server.store.adapter.out.persistence.StoreImagesResponse;
import com.magambell.server.store.app.port.out.response.StorePreSignedUrlImage;
import com.magambell.server.user.domain.entity.User;
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
                        .map(banner -> new BannerPreSignedUrlImage(banner.getOrder(), banner.getName()))
                        .toList();

        return new BannerImagesResponse(images);
    }


//    private EditBannerImageResponseDTO changeBannerImage(final Banner banner,
//                                                       final List<BannerImagesRegister> bannerImagesRegisters) {
//        return bannerCommandPort.editBannerImage(banner, bannerImagesRegisters);
//    }
}
