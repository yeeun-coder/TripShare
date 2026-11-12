# TripMate Spring Boot API 서버

MySQL과 연동하는 REST API입니다. Android 앱(`../app/`)의 `MockData`를 대체할 백엔드입니다.

## 사전 준비

- **Java 17+** (Java 21도 가능)
- **MySQL 8.x** 실행 중

## 1. 데이터베이스 생성

```bash
mysql -u root -p < ../database/tripshare.sql
```

## 2. 접속 설정

MySQL **root 비밀번호**를 설정해야 합니다. (비밀번호 없이 접속 불가)

```bash
cd server/src/main/resources
cp application-local.yml.example application-local.yml
```

`application-local.yml`을 열어 본인 MySQL root 비밀번호를 입력하세요.

```yaml
spring:
  datasource:
    password: YOUR_MYSQL_ROOT_PASSWORD
```

또는 환경 변수로 지정할 수 있습니다.

```bash
export MYSQL_PASSWORD=YOUR_MYSQL_ROOT_PASSWORD
./gradlew bootRun
```

### DB가 아직 없다면

```bash
mysql -u root -p < ../database/tripshare.sql
```

## 3. 실행

```bash
cd server
./gradlew bootRun
```

최초 실행 시 샘플 데이터가 자동 삽입됩니다.

| 계정 | 비밀번호 |
|------|----------|
| yuni@example.com | 1234 |

API: `http://localhost:8080`

## API 요약

| Method | URL |
|--------|-----|
| POST | `/api/auth/signup`, `/api/auth/login` |
| GET | `/api/trips`, `/api/trips/{id}` |
| POST | `/api/trips`, `/api/trips/{id}/reviews`, `/api/trips/{id}/save` |
| GET | `/api/users/{id}/trips/created`, `/api/users/{id}/trips/saved` |
| POST | `/api/users/{id}/follow/{targetId}` |

자세한 요청 예시는 [`../database/README.md`](../database/README.md)를 참고하세요.

## Android 연동

에뮬레이터에서 PC localhost 접근: `http://10.0.2.2:8080`
