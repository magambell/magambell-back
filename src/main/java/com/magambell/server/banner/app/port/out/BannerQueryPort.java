package com.magambell.server.banner.app.port.out;

import com.magambell.server.banner.domain.entity.Banner;

import java.util.List;

public interface BannerQueryPort {

    List<Banner> getBannerList();

    Banner findById(Long bannerId);

}
