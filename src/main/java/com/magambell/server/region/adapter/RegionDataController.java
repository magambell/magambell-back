package com.magambell.server.region.adapter;

import com.magambell.server.common.Response;
import com.magambell.server.region.app.service.RegionDataLoader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Region Data", description = "지역 데이터 관리 API (관리자용)")
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/region")
@RestController
public class RegionDataController {

    private final RegionDataLoader regionDataLoader;

    @Operation(
            summary = "법정동 데이터 로드",
            description = "CSV 파일에서 법정동 코드 데이터를 읽어서 DB에 삽입합니다. (AWS RDS 최적화)"
    )
    @PostMapping("/load")
    public Response<String> loadRegionData() {
        log.info("🚀 Starting region data loading...");
        
        // 프로젝트 루트의 data 폴더에 있는 CSV 파일 경로
        String csvFilePath = "data/국토교통부_전국 법정동_20250807.csv";
        
        regionDataLoader.loadRegionDataFromCsv(csvFilePath);
        
        return new Response<>("법정동 데이터 로드 완료");
    }

    @Operation(
            summary = "법정동 데이터 전체 삭제",
            description = "region 테이블의 모든 데이터를 삭제합니다. (재로드 전 사용)"
    )
    @DeleteMapping("/clear")
    public Response<String> clearRegionData() {
        log.info("🗑️ Starting region data clearing...");
        
        regionDataLoader.clearAllRegionData();
        
        return new Response<>("법정동 데이터 전체 삭제 완료");
    }
}
