# Magambell 백엔드 프로젝트 - 빠진 파일 체크리스트

## ✅ 현재 프로젝트 설정 파일 현황

### 1. 백엔드 설정 파일 (완료)
- [x] `src/main/resources/application.yml` - 메인 설정
- [x] `src/main/resources/firebase_service-account.json` - Firebase 설정
- [x] `src/main/resources/googleOAuth.json` - Google OAuth 설정
- [x] `src/main/resources/logback-spring.xml` - 로깅 설정
- [x] `src/test/resources/logback-test.xml` - 테스트 로깅 설정

### 2. 환경변수 파일 (완료)
- [x] `.env.local` - 로컬 개발 환경
- [x] `.env.dev` - Dev 환경
- [x] `.env.prod` - Prod 환경

### 3. AWS 자격증명 (완료)
- [x] `credentials/magambell-dev-ecr_accessKeys.csv`
- [x] `credentials/magambell-dev-ses_accessKeys.csv`
- [x] `credentials/magambell-ecr_accessKeys.csv`
- [x] `credentials/magambell-ses_accessKeys.csv`

### 4. 프론트엔드/모바일 참고 파일 (완료)
- [x] `docs/frontend-mobile-reference/android-strings.xml`
- [x] `docs/frontend-mobile-reference/.env.mobile`

### 5. Git 설정 (완료)
- [x] `.gitignore` - 업데이트 완료

---

## 🔍 백엔드 코드에서 참조하는 환경변수 확인

### application.yml에 설정된 환경변수
```yaml
# 데이터베이스
SERVER_HOST
MARIA_USERNAME
MARIA_PASSWORD

# JWT
JWT_SECRET_KEY

# AWS
AWS_SES_ACCESS_KEY
AWS_SES_SECRET_KEY
AWS_S3_BUCKET
AWS_CF_DISTRIBUTION

# OAuth
NAVER_CLIENT_ID
NAVER_CLIENT_SECRET

# 결제
PORT_ONE_STORE_ID
PORT_ONE_API_KEY
PORT_ONE_WEB_HOOK

# Firebase
FIREBASE_CONFIG_JSON
```

### 모든 환경변수가 설정됨 ✅
- `.env.local` - 로컬 개발용 (H2 DB, 테스트 키)
- `.env.dev` - Dev 서버용 (MariaDB, Dev AWS 키)
- `.env.prod` - Prod 서버용 (MariaDB, Prod AWS 키)

---

## ⚠️ 주의: 프론트엔드/모바일 프로젝트는 별도 관리 필요

받으신 다음 파일들은 **백엔드가 아닌 프론트엔드/모바일 앱** 프로젝트에 속합니다:

### Android 앱 설정
- `android/app/src/main/res/values/strings.xml`
  - Naver Client ID/Secret
  - Kakao API Key
  - FCM 알림 채널 설정

### 프론트엔드/모바일 환경변수
- `.env` (프론트엔드/모바일 프로젝트 루트)
  - API URL
  - OAuth URL
  - Naver Cloud Platform (지도 API)
  - Kakao JavaScript/Native Key
  - Firebase Android 설정

**백엔드 프로젝트에는 참고용으로만 저장했습니다:**
- `docs/frontend-mobile-reference/android-strings.xml`
- `docs/frontend-mobile-reference/.env.mobile`

---

## 📊 환경변수 비교: 백엔드 vs 프론트엔드

### 백엔드에만 필요한 것
- `SERVER_HOST`, `MARIA_USERNAME`, `MARIA_PASSWORD` - DB 연결
- `AWS_SES_ACCESS_KEY`, `AWS_SES_SECRET_KEY` - 이메일 발송
- `AWS_S3_BUCKET`, `AWS_CF_DISTRIBUTION` - 파일 업로드
- `JWT_SECRET_KEY` - JWT 토큰 생성
- `PORT_ONE_API_KEY`, `PORT_ONE_WEB_HOOK` - 결제 검증
- `FIREBASE_CONFIG_JSON` - FCM 푸시 발송

### 프론트엔드/모바일에만 필요한 것
- `API_URL`, `DEV_API_URL` - 백엔드 API 호출
- `KAKAO_JAVASCRIPT_KEY`, `KAKAO_NATIVE_APP_KEY` - Kakao SDK
- `X_NCP_APIGW_API_KEY_ID`, `X_NCP_APIGW_API_KEY` - Naver 지도 API
- `PORTONE_CHANNEL_KEY`, `PORTONE_IMP_CODE` - 결제 UI
- `FIREBASE_APP_ID_ANDROID_*` - Firebase SDK 초기화

### 양쪽 모두 사용하는 것
- `NAVER_CLIENT_ID`, `NAVER_CLIENT_SECRET` - Naver 로그인
- `PORT_ONE_STORE_ID` - 결제 Store ID

---

## ✅ 결론: 백엔드 프로젝트 설정 완료

### 빌드 가능 상태 확인
```powershell
# 빌드 테스트
.\gradlew clean build

# 로컬 실행
.\gradlew bootRun

# 포트 확인
# - API: http://localhost:8080
# - Swagger: http://localhost:8080/swagger-ui/index.html
# - H2 Console: http://localhost:8080/h2-console
```

### 추가 작업 필요사항
1. **GitHub Actions Secrets 설정**
   - `.env.dev`와 `.env.prod`의 환경변수를 GitHub Secrets에 등록
   - `EC2_HOST`, `EC2_USERNAME`, `EC2_SSH_KEY` 추가

2. **EC2 서버 배포 스크립트 확인**
   - `/home/ubuntu/deploy.sh` 파일 존재 여부 확인
   - Blue-Green 배포 로직 검증

3. **프론트엔드/모바일 프로젝트 별도 관리**
   - Android 프로젝트의 `strings.xml` 설정
   - React Native/Flutter 프로젝트의 `.env` 설정

---

**작성일**: 2024-12-06  
**상태**: 백엔드 설정 완료 ✅
