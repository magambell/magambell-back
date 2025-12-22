package com.magambell.server.region.app.service;

import com.magambell.server.region.app.port.in.RegionUseCase;
import com.magambell.server.region.app.port.out.response.EupmyeondongListResponse;
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
        List<String> sidoList = regionRepository.findDistinctSido();
        return SidoListResponse.of(sidoList);
    }

    @Override
    public SigunguListResponse getSigunguList(String sido) {
        List<String> sigunguList = regionRepository.findDistinctSigunguBySido(sido);
        return SigunguListResponse.of(sigunguList);
    }

    @Override
    public EupmyeondongListResponse getEupmyeondongList(String sido, String sigungu) {
        List<String> eupmyeondongList = regionRepository.findDistinctEupmyeondongBySidoAndSigungu(sido, sigungu);
        return EupmyeondongListResponse.of(eupmyeondongList);
    }
}
