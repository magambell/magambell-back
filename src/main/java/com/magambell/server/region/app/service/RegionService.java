package com.magambell.server.region.app.service;

import com.magambell.server.region.app.port.in.RegionUseCase;
import com.magambell.server.region.app.port.out.response.EupmyeondongListResponse;
import com.magambell.server.region.app.port.out.response.EupmyeondongListResponse.TownDTO;
import com.magambell.server.region.app.port.out.response.SidoListResponse;
import com.magambell.server.region.app.port.out.response.SigunguListResponse;
import com.magambell.server.region.domain.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RegionService implements RegionUseCase {

    private final RegionRepository regionRepository;

    @Override
    public SidoListResponse getSidoList() {
        List<String> cityList = regionRepository.findDistinctCity();
        return SidoListResponse.of(cityList);
    }

    @Override
    public SigunguListResponse getSigunguList(String sido) {
        List<String> districtList = regionRepository.findDistinctDistrictByCity(sido);
        return SigunguListResponse.of(districtList);
    }

    @Override
    public EupmyeondongListResponse getEupmyeondongList(String sido, String sigungu) {
        List<TownDTO> townList = regionRepository.findDistinctTownByCityAndDistrict(sido, sigungu);
        return EupmyeondongListResponse.of(townList);
    }
}
