package com.magambell.server.region.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegionDataLoader {

    private final JdbcTemplate jdbcTemplate;

    /**
     * CSV 파일에서 법정동 데이터를 읽어서 DB에 배치 삽입
     * AWS RDS 환경에 최적화된 방식
     */
    @Transactional
    public void loadRegionDataFromCsv(String csvFilePath) {
        String sql = "INSERT INTO region (legal_code, sido, sigungu, eupmyeondong, ri, is_deleted) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        int totalCount = 0;
        int batchSize = 1000;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); // 헤더 스킵 (법정동코드,시도명,시군구명,읍면동명,리명,순위,생성일자,삭제일자,과거법정동코드)

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // -1을 사용하여 빈 문자열도 포함

                if (values.length < 9) {
                    log.warn("Invalid line skipped: {}", line);
                    continue;
                }

                Object[] args = new Object[6];
                args[0] = values[0].trim(); // legal_code
                args[1] = values[1].trim(); // sido
                args[2] = values[2].trim().isEmpty() ? null : values[2].trim(); // sigungu
                args[3] = values[3].trim().isEmpty() ? null : values[3].trim(); // eupmyeondong
                args[4] = values[4].trim().isEmpty() ? null : values[4].trim(); // ri
                // 삭제일자(index 7)가 있으면 폐지된 것으로 간주
                args[5] = !values[7].trim().isEmpty(); // is_deleted

                batchArgs.add(args);

                // 1000개씩 배치 삽입
                if (batchArgs.size() >= batchSize) {
                    jdbcTemplate.batchUpdate(sql, batchArgs);
                    totalCount += batchArgs.size();
                    log.info("Inserted {} rows (total: {})", batchArgs.size(), totalCount);
                    batchArgs.clear();
                }
            }

            // 남은 데이터 삽입
            if (!batchArgs.isEmpty()) {
                jdbcTemplate.batchUpdate(sql, batchArgs);
                totalCount += batchArgs.size();
                log.info("Inserted {} rows (total: {})", batchArgs.size(), totalCount);
            }

            log.info("✅ Region data loading completed! Total {} rows inserted.", totalCount);

        } catch (Exception e) {
            log.error("❌ Failed to load region data from CSV: {}", e.getMessage(), e);
            throw new RuntimeException("Region data loading failed", e);
        }
    }

    /**
     * 기존 데이터 전체 삭제 (재삽입 시 사용)
     */
    @Transactional
    public void clearAllRegionData() {
        int deletedCount = jdbcTemplate.update("DELETE FROM region");
        log.info("🗑️ Cleared all region data: {} rows deleted", deletedCount);
    }
}
