-- ============================================
-- RDS 컬럼명 변경 SQL
-- sido -> city, sigungu -> district, eupmyeondong -> town
-- ============================================

-- 주의: 인덱스는 기존 이름 그대로 유지하거나, 필요시 삭제 후 재생성

-- 1. sido 컬럼을 city로 변경
ALTER TABLE region CHANGE COLUMN sido city VARCHAR(50) NOT NULL;

-- 2. sigungu 컬럼을 district로 변경
ALTER TABLE region CHANGE COLUMN sigungu district VARCHAR(50);

-- 3. eupmyeondong 컬럼을 town으로 변경
ALTER TABLE region CHANGE COLUMN eupmyeondong town VARCHAR(50);


-- ============================================
-- 인덱스 재생성 (선택사항 - 기존 인덱스 삭제 후)
-- ============================================

-- 기존 인덱스 삭제
DROP INDEX idx_sido ON region;
DROP INDEX idx_sido_sigungu ON region;
DROP INDEX idx_sido_sigungu_eupmyeondong ON region;

-- 새 인덱스 생성
CREATE INDEX idx_city ON region(city);
CREATE INDEX idx_city_district ON region(city, district);
CREATE INDEX idx_city_district_town ON region(city, district, town);


-- ============================================
-- 변경 확인
-- ============================================

-- 테이블 구조 확인
DESC region;

-- 인덱스 확인
SHOW INDEX FROM region;

-- 데이터 확인
SELECT * FROM region LIMIT 10;
