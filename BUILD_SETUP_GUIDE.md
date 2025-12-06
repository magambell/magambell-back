# Magambell 백엔드 설정 가이드

## 📁 생성된 파일 구조

```
magambell-back/
├── .env.local                                    # 로컬 개발 환경변수
├── .env.dev                                      # Dev 환경변수
├── .env.prod                                     # Prod 환경변수
├── credentials/                                  # AWS 액세스 키 (Git 무시됨)
│   ├── magambell-dev-ecr_accessKeys.csv
│   ├── magambell-dev-ses_accessKeys.csv
│   ├── magambell-ecr_accessKeys.csv
│   └── magambell-ses_accessKeys.csv
└── src/main/resources/
    ├── firebase_service-account.json             # Firebase 서비스 계정 (Git 무시됨)
    └── googleOAuth.json                          # Google OAuth 설정 (Git 무시됨)
```

## 🚀 로컬 개발 환경 빌드 방법

### 1. 로컬 환경 (H2 데이터베이스 사용)

```powershell
# 기본적으로 로컬 프로파일로 실행됩니다
.\gradlew clean build
.\gradlew bootRun
```

또는 환경변수 파일을 사용하려면:

```powershell
# .env.local 파일의 환경변수를 로드
Get-Content .env.local | ForEach-Object {
    if ($_ -match '^([^=]+)=(.*)$') {
        [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process')
    }
}

# 빌드 및 실행
.\gradlew bootRun
```

### 2. Dev 환경으로 로컬에서 테스트

```powershell
# .env.dev 파일의 환경변수를 로드
Get-Content .env.dev | ForEach-Object {
    if ($_ -match '^([^=]+)=(.*)$') {
        [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process')
    }
}

# 또는 직접 환경변수 설정
$env:SPRING_PROFILES_ACTIVE = "dev"
$env:SERVER_HOST = "magambell-server.cdci0ms4iz13.ap-northeast-2.rds.amazonaws.com"
$env:MARIA_USERNAME = "admin"
$env:MARIA_PASSWORD = "akrkaqpf24!"

# 빌드 및 실행
.\gradlew bootRun
```

### 3. IntelliJ IDEA 설정

**방법 1: Environment variables 파일 사용**
1. Run → Edit Configurations
2. Environment variables: `.env.local` 파일 경로 지정
3. 또는 IntelliJ Plugin "EnvFile" 설치

**방법 2: VM Options 사용**
```
-Dspring.profiles.active=local
-Djwt.secret-key=ZSI1IkphdmFJblVzZSIsIsV4cCI6MTY5NzU5NjI5OCwiaWF0IjoxNjk3NTk2Mjk4fQ.yP9ujqZJyo7TyoGzPbiQwCY_B-JJWtKWLDFa6q6IoI8
```

## 🔍 접속 URL

### 로컬 개발 환경
- **API 서버**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **H2 Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:file:./data/magambell-db`
  - Username: `sa`
  - Password: (비워둠)
- **Actuator Health**: `http://localhost:8080/magambell/health`

### Dev 환경
- **API 서버**: `https://dev.mgbell-server.run`
- **Swagger UI**: `https://dev.mgbell-server.run/swagger-ui/index.html`
- **Actuator Health**: `https://dev.mgbell-server.run/magambell/health`

### Prod 환경
- **API 서버**: `https://mgbell-server.run` (추정)
- **Swagger UI**: 비활성화됨
- **Actuator Health**: `https://mgbell-server.run/magambell/health`

## 📋 환경별 주요 차이점

| 항목 | Local | Dev | Prod |
|------|-------|-----|------|
| 데이터베이스 | H2 (파일) | MariaDB (RDS) | MariaDB (RDS) |
| DDL 모드 | update | none | none |
| Swagger | 활성화 | 활성화 | 비활성화 |
| H2 Console | 활성화 | 비활성화 | 비활성화 |
| SQL 로깅 | 활성화 | 비활성화 | 비활성화 |
| AWS SES | 테스트 키 | Dev 키 | Prod 키 |

## ⚠️ 중요 보안 사항

### .gitignore 업데이트 완료
다음 파일들이 Git에서 제외되도록 설정되었습니다:
- `.env*` - 모든 환경변수 파일
- `/credentials/` - AWS 액세스 키 파일들
- `/src/main/resources/firebase_service-account.json`
- `/src/main/resources/googleOAuth.json`

### Git에 민감정보 제거 (이미 커밋된 경우)
```powershell
# 캐시에서 제거
git rm --cached src/main/resources/firebase_service-account.json
git rm --cached src/main/resources/googleOAuth.json
git rm --cached -r credentials/

# 커밋 및 푸시
git commit -m "Remove sensitive credentials from repository"
git push
```

## 🔧 빌드 문제 해결

### 1. QueryDSL Q클래스 생성 안 됨
```powershell
.\gradlew clean build --refresh-dependencies
```

### 2. 환경변수 로드 안 됨
```powershell
# PowerShell에서 환경변수 확인
Get-ChildItem Env: | Where-Object { $_.Name -like "AWS_*" -or $_.Name -like "JWT_*" }
```

### 3. H2 데이터베이스 파일 권한 오류
```powershell
# data 폴더 삭제 후 재생성
Remove-Item -Recurse -Force ./data
.\gradlew bootRun
```

### 4. MariaDB 연결 실패 (Dev/Prod)
- RDS 보안 그룹 인바운드 규칙 확인 (3306 포트)
- VPC 네트워크 설정 확인
- 데이터베이스 계정 권한 확인

## 📦 CI/CD 배포 설정

### GitHub Actions Secrets 설정 필요 항목

**Repository Settings → Secrets and variables → Actions** 에 다음 항목 추가:

```
# AWS 자격증명
AWS_ACCESS_KEY_ID=your_aws_access_key_here
AWS_SECRET_ACCESS_KEY=your_aws_secret_key_here

# 데이터베이스
SERVER_HOST=your_database_host_here
MARIA_USERNAME=your_db_username
MARIA_PASSWORD=your_db_password

# JWT
JWT_SECRET_KEY=your_jwt_secret_key_here

# AWS 서비스
AWS_S3_BUCKET=your_s3_bucket_name
AWS_CF_DISTRIBUTION=your_cloudfront_distribution
AWS_SES_ACCESS_KEY=your_ses_access_key_here
AWS_SES_SECRET_KEY=your_ses_secret_key_here

# OAuth
NAVER_CLIENT_ID=BVHH2fcyVFbjZQFCNMY7
NAVER_CLIENT_SECRET=Lhpj63W9LZ

# 결제
PORT_ONE_STORE_ID=store-7ddd1a03-2dc8-430d-8a1a-f9bd06722804
PORT_ONE_API_KEY=oAjv0YYrlUgorIemP6NGMRR2XSOHcbXUHxEU6amWwJux1IBvdSHIMPixkF3hfw3wwq0DyrkYGsg4pzsi
PORT_ONE_WEB_HOOK=whsec_+fxsTRW4/KJsPoccO7CGomLup9TVRXQBNXoy7NHp1YI=

# Firebase (Base64 인코딩됨)
FIREBASE_CONFIG_JSON=ewogICJ0eXBlIjogInNlcnZpY2VfYWNjb3VudCIsCiAgInByb2plY3RfaWQiOiAibWFnYW1iZWxsLTkzMzIwIiwKICAicHJpdmF0ZV9rZXlfaWQiOiAiNTFkMjdkMWZmYzM1ZWVkMDc5ZWU0MTU4ODg4ODg0MjY1NDIxOTQ1NyIsCiAgInByaXZhdGVfa2V5IjogIi0tLS0tQkVHSU4gUFJJVkFURSBLRVktLS0tLVxuTUlJRXZRSUJBREFOQmdrcWhraUc5dzBCQVFFRkFBU0NCS2N3Z2dTakFnRUFBb0lCQVFDb3JqVU9leGtISUVwclxuYnFnWmI0RTB1ZUFSOHdiL3N4cTUrdnVLa0Q4NWVlMkt0TGJCU0NSSzVDeXhIZlMxWlJ5bXZSR2RSZGprMmtZNFxuMThSRWZuZEZrYy9tRnR0b3FENU1mU0VxZi9Iem9YSVlLeGJIdnE0SWNSOE1vVWthYURJcGtwMC9CeXVOR3ErZlxud0hVOWU1SFRDbUhLUkR5ZVNiNTBkUEczbjhOZjB2TVQvZ2FZR1IwRnROa1dIV3Q0MjJXQ3pSSU03ZVVRdXB2Z1xub015RkJLMzZaU3YzbjJTdWZKYnRWdldRTjkwWnVvZzJ2Y29Eenc1ZUpCbTRkWVdrUWMvMFl3NmpSZko0NytKOVxuR05WSW5kWHFZeVZ1dzdIZFhqbFRuNDNRV1FISDFYMU8renQ4dWQ2TnF4R3BPVlFVMjUwM3BHZDRQU2F3RSt3cFxuR3NKY2RFWEpBZ01CQUFFQ2dnRUFBcUkrdHBXZzRmU1BmajdhOHlrT0xNazFYaE8ycHMzYktOblVyVGk4U0tlQlxuQXZiaStYWUkzVjVUblh2YzRjQ3lOUFJ6NFdvUlBWM2U5YjFXWlpDT2N2TllXa3krSmdrUXprcGF0d0oxVldNalxuMHVGZHNKRW5WVDhwUWozdlNaRTUyRWpxRHVLcjBSOFZySUpFS2w2NFc3Y3RxS0RPWVZVcTJ4Vm1NL2UrV2V2Y1xuMWFLNEQ5Z3BOSlJScW9sM0VkWFJydXNDZlZjYkNxbXI5azhXejBlVyszOUJMWDBuaUpmekpkWDgvTjEvekM2dlxuR1pIMkxYNHlWT2NpZE1KZldobkNaYUhOZHdIVUlSS0ovS0JpYTlxYi9Fc1lSV2hFRTVBeUt6WEdaNnpaY09IcVxuSlhlVnpiYS9neXhiUTJ5YWhiZ00zaEs1TCtLZkhaTkwxVUxuZW5iK3dRS0JnUURjTHB2eVIwTVlOdTZ1SmdwVFxuUURDUkZ3VTBTNUZDTTRKODE3Q2tnMFZLa1Z6KzlyRzFHS0V3WENwMHdsMDRKZ3pIUDBPcFQ2QUk2aWx0bVNtR1xucENyYXB5dnh6QWpHNTBMbzExcjlwNmNENlJmczBkeHVVMUloSk0zSXg0aXF1WVBXcldIczRWcStyZnM4aG9ESlxuUzc2Y013cGpnZHZ0TitWWGtNUkNzNHlBQ1FLQmdRREVIdFlwa0lzSnJaYTYwc1dBL1ZUR3FpdmxZYVdYVjFWc1xuT29MOUZUYWFhb3Z4SnI4Z2tmNXlxUWdGb1d1Y005ZWcvRGNIbi90ODRzTWQra0JvZnlrQXBpekNJMXFJQ3cwSlxuaUtEc3lCNTE5em1oWDFIODBIUjJldDEvdjdzZWlDTWg0YWgvK1BtNkdyS3NYRE03N29HbFR5Rmw4M0pKeG5kc1xuc0VEaDBxdUh3UUtCZ0F3amNhdDRyRVZBZWdZVnlNbndKdjFJdEp1K2NzS2dqbGdObUFwcHhqOG5KbVpUK1ZZUFxuaG90ZytXSTRlT1VvaTluTUxoRytkd2NteWFQbUpyanFnZ1pONHdsUWcwZGI1bjlwcC9XRjhab3dsb1lTeDhFNVxuVExUUkQ1czdETmpFbzNVSlYvVzBNc21DVS9CeFJkWlRHNjRHemxMUURPeldBUW5EekY0VkFmRHBBb0dCQU1GblxuTzRHUHhsMUw3Mm5iY3VQS2IzRGUzaHo5MTZQUWZ6QVUzNW9lOGxNRElYQTE2cU4rOWJNekYvTWRoWlpyMVlndlxuVVVGV3RsWW85WXU1Znd6TjRheC9NNEpjQk9wR0k1dzBQYVpqanc1OE9EdjRXVXo4Y08xcUtzYlJXQUU2OVpKelxuQzR6d0JhMU9xTWFxd2VlblEvaTJlVlFsNkozeWQxRWEyMUVYUlFyQkFvR0FaMWQzVzlXcSt5Y3MydGNrQ1Q0YlxuUThJWVltYkNHS0FaZHpvdzdvZFpRdUdNTTJ2SWxXQlI5M2NqemcxV2JOd3JJMDdqUWFHUEV6NEMvZUVrSUVHRlxueGMrS1dja0VOWDJhNUFSUkt1VHp5TG1CL2JOeGFYQU4yS1hXU0QwQWRHemtIZG94bmlHRVhtMjYzczN0eGZ1a1xuTkdBWHZSaDlzc3hhQ05kajlBQ0FENEE9XG4tLS0tLUVORCBQUklWQVRFIEtFWS0tLS0tXG4iLAogICJjbGllbnRfZW1haWwiOiAiZmlyZWJhc2UtYWRtaW5zZGstZmJzdmNAbWFnYW1iZWxsLTkzMzIwLmlhbS5nc2VydmljZWFjY291bnQuY29tIiwKICAiY2xpZW50X2lkIjogIjExMzIzMTA1MDI4ODUxMjI5Mzg1OCIsCiAgImF1dGhfdXJpIjogImh0dHBzOi8vYWNjb3VudHMuZ29vZ2xlLmNvbS9vL29hdXRoMi9hdXRoIiwKICAidG9rZW5fdXJpIjogImh0dHBzOi8vb2F1dGgyLmdvb2dsZWFwaXMuY29tL3Rva2VuIiwKICAiYXV0aF9wcm92aWRlcl94NTA5X2NlcnRfdXJsIjogImh0dHBzOi8vd3d3Lmdvb2dsZWFwaXMuY29tL29hdXRoMi92MS9jZXJ0cyIsCiAgImNsaWVudF94NTA5X2NlcnRfdXJsIjogImh0dHBzOi8vd3d3Lmdvb2dsZWFwaXMuY29tL3JvYm90L3YxL21ldGFkYXRhL3g1MDkvZmlyZWJhc2UtYWRtaW5zZGstZmJzdmMlNDBtYWdhbWJlbGwtOTMzMjAuaWFtLmdzZXJ2aWNlYWNjb3VudC5jb20iLAogICJ1bml2ZXJzZV9kb21haW4iOiAiZ29vZ2xlYXBpcy5jb20iCn0K

# EC2 SSH (배포용)
EC2_HOST=<EC2 퍼블릭 IP 또는 도메인>
EC2_USERNAME=ubuntu
EC2_SSH_KEY=<SSH Private Key 내용>
```

## 🎯 다음 단계

1. **로컬 빌드 테스트**
   ```powershell
   .\gradlew clean build
   .\gradlew bootRun
   ```

2. **Swagger 접속 확인**
   - `http://localhost:8080/swagger-ui/index.html`

3. **H2 데이터베이스 확인**
   - `http://localhost:8080/h2-console`

4. **API 테스트**
   - Health Check: `curl http://localhost:8080/magambell/health`

5. **GitHub Actions 워크플로우 확인**
   - `.github/workflows/deploy-dev.yml`
   - `.github/workflows/deploy-prod.yml`

## 📞 문제 발생 시 체크리스트

- [ ] Java 17 설치 확인: `java -version`
- [ ] Gradle 빌드 성공: `.\gradlew clean build`
- [ ] 환경변수 로드 확인: `Get-ChildItem Env:`
- [ ] 포트 8080 사용 가능: `netstat -ano | findstr :8080`
- [ ] 방화벽 설정 확인 (Windows Defender)
- [ ] .gitignore에 민감정보 제외 확인
- [ ] GitHub Secrets 설정 완료 확인

---

**생성 일시**: 2024-12-06  
**작성자**: GitHub Copilot
