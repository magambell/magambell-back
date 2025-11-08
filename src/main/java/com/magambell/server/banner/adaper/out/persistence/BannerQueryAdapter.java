package com.magambell.server.banner.adaper.out.persistence;

import com.magambell.server.banner.app.port.out.BannerQueryPort;
import com.magambell.server.banner.domain.entity.Banner;
import com.magambell.server.banner.domain.repository.BannerRepository;
import com.magambell.server.common.annotation.Adapter;
import com.magambell.server.common.enums.ErrorCode;
import com.magambell.server.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Adapter
public class BannerQueryAdapter implements BannerQueryPort {

    private final BannerRepository bannerRepository;

    @Override
    public List<Banner> getBannerList() {
        return bannerRepository.getBannerList();
    }

    @Override
    public Banner findById(Long bannerId) {
        return bannerRepository.findById(bannerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BANNER_NOT_FOUND));
    }

}
