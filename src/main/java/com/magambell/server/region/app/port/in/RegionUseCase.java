package com.magambell.server.region.app.port.in;

import com.magambell.server.region.app.port.out.response.EupmyeondongListResponse;
import com.magambell.server.region.app.port.out.response.SidoListResponse;
import com.magambell.server.region.app.port.out.response.SigunguListResponse;

public interface RegionUseCase {

    /**
     * 시·도 목록 조회
     */
    SidoListResponse getSidoList();

    /**
     * 시·군·구 목록 조회
     */
    SigunguListResponse getSigunguList(String sido);

    /**
     * 읍·면·동 목록 조회
     */
    EupmyeondongListResponse getEupmyeondongList(String sido, String sigungu);
}
