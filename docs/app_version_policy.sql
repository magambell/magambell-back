-- 앱 버전 정책 테이블 생성
CREATE TABLE app_version_policy (
    policy_id BIGINT PRIMARY KEY,
    platform VARCHAR(20) NOT NULL UNIQUE,
    latest_version VARCHAR(20) NOT NULL COMMENT '최신 버전',
    min_supported_version VARCHAR(20) NOT NULL COMMENT '최소 지원 버전 (이 미만은 강제 업데이트)',
    recommended_min_version VARCHAR(20) COMMENT '권장 최소 버전 (이 미만은 업데이트 권장)',
    force_update BOOLEAN DEFAULT FALSE COMMENT '강제 업데이트 여부',
    android_store_url VARCHAR(500) COMMENT 'Play Store URL',
    ios_store_url VARCHAR(500) COMMENT 'App Store URL',
    release_notes TEXT COMMENT '릴리스 노트 (선택 필드)',
    active BOOLEAN DEFAULT TRUE COMMENT '활성 상태',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    modified_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 초기 데이터 삽입
INSERT INTO app_version_policy (
    policy_id, 
    platform, 
    latest_version, 
    min_supported_version, 
    recommended_min_version, 
    force_update, 
    android_store_url, 
    ios_store_url,
    active
) VALUES 
(1, 'ANDROID', '1.0.0', '1.0.0', '1.0.0', false, 'https://play.google.com/store/apps/details?id=com.magambell', null, true),
(2, 'IOS', '1.0.0', '1.0.0', '1.0.0', false, null, 'https://apps.apple.com/app/id000000000', true);
