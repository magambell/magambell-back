-- ============================================
-- 법정동 코드 기반 지역 정보 테이블
-- ============================================

-- 테이블 생성
CREATE TABLE region (
    region_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '지역 ID (PK)',
    legal_code VARCHAR(10) NOT NULL UNIQUE COMMENT '법정동 코드 (10자리)',
    sido VARCHAR(50) NOT NULL COMMENT '시·도',
    sigungu VARCHAR(50) COMMENT '시·군·구',
    eupmyeondong VARCHAR(50) COMMENT '읍·면·동',
    ri VARCHAR(50) COMMENT '리',
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE COMMENT '폐지 여부',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    modified_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    INDEX idx_sido (sido),
    INDEX idx_sido_sigungu (sido, sigungu),
    INDEX idx_sido_sigungu_eupmyeondong (sido, sigungu, eupmyeondong)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='법정동 코드 기반 지역 정보';


-- ============================================
-- CSV 데이터 삽입을 위한 SQL 가이드
-- ============================================

-- CSV 파일 구조 (국토교통부_전국 법정동_20250807.csv):
-- 법정동코드,시도명,시군구명,읍면동명,리명,순위,생성일자,삭제일자,과거법정동코드
-- 1100000000,서울특별시,,,,11,1988-04-23,,
-- 1111000000,서울특별시,종로구,,,1,1988-04-23,,
-- 1111010100,서울특별시,종로구,청운동,,1,1988-04-23,,

-- 방법 1: LOAD DATA INFILE 사용 (MySQL 서버 로컬 파일)
-- 주의: MySQL 서버가 있는 서버의 파일 시스템에 CSV 파일이 있어야 합니다.
-- 파일 경로는 실제 환경에 맞게 수정해주세요.

LOAD DATA LOCAL INFILE '/path/to/국토교통부_전국 법정동_20250807.csv'
INTO TABLE region
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(legal_code, sido, @sigungu, @eupmyeondong, @ri, @rank, @created_date, @deleted_date, @old_legal_code)
SET 
    sigungu = NULLIF(TRIM(@sigungu), ''),
    eupmyeondong = NULLIF(TRIM(@eupmyeondong), ''),
    ri = NULLIF(TRIM(@ri), ''),
    is_deleted = IF(TRIM(@deleted_date) != '', TRUE, FALSE),
    created_at = NOW(),
    modified_at = NOW();


-- 방법 2: Python 스크립트를 사용한 데이터 삽입
-- 아래 Python 스크립트를 참고하세요.

/*
import csv
import pymysql

# DB 연결
connection = pymysql.connect(
    host='localhost',
    user='your_username',
    password='your_password',
    database='your_database',
    charset='utf8mb4'
)

try:
    with connection.cursor() as cursor:
        # CSV 파일 읽기
        with open('data/국토교통부_전국 법정동_20250807.csv', 'r', encoding='utf-8') as file:
            csv_reader = csv.reader(file)
            next(csv_reader)  # 헤더 스킵
            
            batch_data = []
            for row in csv_reader:
                legal_code = row[0].strip()
                sido = row[1].strip()
                sigungu = row[2].strip() if row[2].strip() else None
                eupmyeondong = row[3].strip() if row[3].strip() else None
                ri = row[4].strip() if row[4].strip() else None
                # 삭제일자가 있으면 폐지된 것
                deleted_date = row[7].strip()
                is_deleted = True if deleted_date else False
                
                batch_data.append((legal_code, sido, sigungu, eupmyeondong, ri, is_deleted))
                
                # 1000개씩 배치 삽입
                if len(batch_data) >= 1000:
                    sql = """
                        INSERT INTO region (legal_code, sido, sigungu, eupmyeondong, ri, is_deleted)
                        VALUES (%s, %s, %s, %s, %s, %s)
                    """
                    cursor.executemany(sql, batch_data)
                    connection.commit()
                    batch_data = []
                    print(f"Inserted {cursor.rowcount} rows")
            
            # 남은 데이터 삽입
            if batch_data:
                sql = """
                    INSERT INTO region (legal_code, sido, sigungu, eupmyeondong, ri, is_deleted)
                    VALUES (%s, %s, %s, %s, %s, %s)
                """
                cursor.executemany(sql, batch_data)
                connection.commit()
                print(f"Inserted {cursor.rowcount} rows")
                
    print("Data insertion completed!")
    
finally:
    connection.close()
*/


-- 방법 3: Java 애플리케이션에서 배치 삽입
-- Spring Batch나 JdbcTemplate의 batchUpdate를 사용하는 것을 권장합니다.

/*
@Service
public class RegionDataLoader {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void loadRegionData(String csvFilePath) throws Exception {
        String sql = "INSERT INTO region (legal_code, sido, sigungu, eupmyeondong, ri, is_deleted) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        List<Object[]> batchArgs = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); // 헤더 스킵
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // -1 to include empty strings
                
                Object[] args = new Object[6];
                args[0] = values[0].trim(); // legal_code
                args[1] = values[1].trim(); // sido
                args[2] = values[2].trim().isEmpty() ? null : values[2].trim(); // sigungu
                args[3] = values[3].trim().isEmpty() ? null : values[3].trim(); // eupmyeondong
                args[4] = values[4].trim().isEmpty() ? null : values[4].trim(); // ri
                // 삭제일자(index 7)가 있으면 폐지된 것
                args[5] = values.length > 7 && !values[7].trim().isEmpty(); // is_deleted
                
                batchArgs.add(args);
                
                if (batchArgs.size() >= 1000) {
                    jdbcTemplate.batchUpdate(sql, batchArgs);
                    batchArgs.clear();
                }
            }
            
            if (!batchArgs.isEmpty()) {
                jdbcTemplate.batchUpdate(sql, batchArgs);
            }
        }
    }
}
*/


-- ============================================
-- 데이터 확인 쿼리
-- ============================================

-- 전체 데이터 개수 확인
SELECT COUNT(*) FROM region;

-- 시·도별 개수 확인
SELECT sido, COUNT(*) as cnt 
FROM region 
WHERE is_deleted = FALSE 
GROUP BY sido 
ORDER BY sido;

-- 시·도 목록 확인
SELECT DISTINCT sido 
FROM region 
WHERE is_deleted = FALSE 
ORDER BY sido;

-- 특정 시·도의 시·군·구 목록 확인
SELECT DISTINCT sigungu 
FROM region 
WHERE sido = '서울특별시' 
  AND sigungu IS NOT NULL 
  AND sigungu != '' 
  AND is_deleted = FALSE 
ORDER BY sigungu;

-- 특정 시·군·구의 읍·면·동 목록 확인
SELECT DISTINCT eupmyeondong 
FROM region 
WHERE sido = '서울특별시' 
  AND sigungu = '강남구' 
  AND eupmyeondong IS NOT NULL 
  AND eupmyeondong != '' 
  AND is_deleted = FALSE 
ORDER BY eupmyeondong;
