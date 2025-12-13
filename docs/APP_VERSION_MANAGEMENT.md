# 앱 버전 관리 기능

## 개요
사용자별로 버전을 DB에 저장하지 않고도 앱 업데이트 권장/강제를 구현한 시스템입니다.

## 구조

### 1. 데이터베이스 테이블
```sql
-- docs/app_version_policy.sql 참고
CREATE TABLE app_version_policy (
    policy_id BIGINT PRIMARY KEY,
    platform VARCHAR(20) NOT NULL UNIQUE,
    latest_version VARCHAR(20) NOT NULL,
    min_supported_version VARCHAR(20) NOT NULL,
    recommended_min_version VARCHAR(20),
    force_update BOOLEAN DEFAULT FALSE,
    android_store_url VARCHAR(500),
    ios_store_url VARCHAR(500),
    release_notes TEXT,  -- 선택 필드
    created_at DATETIME,
    modified_at DATETIME
);
```

### 2. 동작 방식

#### 클라이언트 요청
모든 API 요청 시 헤더에 포함:
```
X-App-Version: 1.0.0
X-Platform: ANDROID  (또는 IOS)
```

#### 서버 응답

**A) 강제 업데이트 필요 (현재 버전 < min_supported_version)**
```json
HTTP 426 Upgrade Required
{
  "message": "앱 업데이트가 필요합니다",
  "data": {
    "forceUpdate": true,
    "latestVersion": "1.2.0",
    "currentVersion": "1.0.0",
    "storeUrl": "https://play.google.com/...",
    "releaseNotes": "중요한 보안 업데이트"  // 선택 필드
  }
}
```

**B) 업데이트 권장 (현재 버전 < recommended_min_version)**
```json
HTTP 200 OK
X-Update-Available: true
X-Update-Info: {"recommended": true, "latestVersion": "1.2.0", ...}

{
  "message": "성공",
  "data": { ... }
}
```

**C) 정상 (현재 버전 >= recommended_min_version)**
```json
HTTP 200 OK
{
  "message": "성공",
  "data": { ... }
}
```

### 3. 버전 정책 업데이트

DB에서 직접 수정 (서버 재시작 불필요):

```sql
-- Android 최소 지원 버전을 1.1.0으로 변경
UPDATE app_version_policy 
SET min_supported_version = '1.1.0',
    latest_version = '1.2.0',
    release_notes = '성능 개선 및 버그 수정'
WHERE platform = 'ANDROID';
```

### 4. 주요 파일

- **엔티티**: `appversion/domain/entity/AppVersionPolicy.java`
- **필터**: `common/security/AppVersionCheckFilter.java`
- **DTO**: `appversion/app/port/in/dto/AppUpdateInfo.java`
- **SQL**: `docs/app_version_policy.sql`

## 설정 방법

### 1. 테이블 생성
```bash
# docs/app_version_policy.sql 실행
mysql -u root -p magambell < docs/app_version_policy.sql
```

### 2. 초기 버전 설정
```sql
-- Android
UPDATE app_version_policy 
SET latest_version = '1.0.0',
    min_supported_version = '1.0.0',
    android_store_url = 'https://play.google.com/store/apps/details?id=com.magambell'
WHERE platform = 'ANDROID';

-- iOS
UPDATE app_version_policy 
SET latest_version = '1.0.0',
    min_supported_version = '1.0.0',
    ios_store_url = 'https://apps.apple.com/app/id000000000'
WHERE platform = 'IOS';
```

## 특징

✅ **releaseNotes는 선택 필드**
- DB에서 NULL 허용
- DTO에서 JsonInclude로 null이면 응답에서 제외
- 업데이트 요청 시 releaseNotes 없이 전송 가능

✅ **사용자별 버전 저장 불필요**
- 요청 헤더에서 실시간으로 확인
- DB 부담 최소화

✅ **플랫폼별 독립 관리**
- Android/iOS 각각 다른 버전 정책 적용 가능

✅ **유연한 업데이트 전략**
- 강제 업데이트: min_supported_version
- 권장 업데이트: recommended_min_version
- 단계적 배포 가능

## 테스트 방법

### Swagger 테스트
```bash
# 헤더 추가
X-App-Version: 0.9.0  # 강제 업데이트 트리거
X-Platform: ANDROID

# 또는
X-App-Version: 1.1.0  # 권장 업데이트 트리거
X-Platform: IOS
```

### curl 테스트
```bash
curl -H "X-App-Version: 0.9.0" \
     -H "X-Platform: ANDROID" \
     -H "Authorization: Bearer YOUR_TOKEN" \
     https://dev.mgbell-server.run/api/v1/user/info
```
