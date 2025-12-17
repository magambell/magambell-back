package com.magambell.server.servicearea;

import com.magambell.server.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ServiceArea", description = "서비스 지역 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/service-areas")
@RestController
public class ServiceAreaController {

    private static final List<ServiceAreaResponse> SERVICE_AREAS = List.of(
            ServiceAreaResponse.of(
                    "경기도 용인시 기흥구 죽전동",
                    "경기도 용인시 기흥구 죽전동",
                    37.331005,
                    127.113871
            ),
            ServiceAreaResponse.of(
                    "경기도 용인시 기흥구 보정동",
                    "경기도 용인시 기흥구 보정동",
                    37.320165,
                    127.112962
            )
    );

    @Operation(summary = "서비스 중인 지역 목록 조회", description = "현재 서비스 중인 모든 지역의 목록을 반환합니다.")
    @GetMapping
    public Response<List<ServiceAreaResponse>> getServiceAreas() {
        return new Response<>(SERVICE_AREAS);
    }
}
