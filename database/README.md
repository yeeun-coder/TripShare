# TripMate MySQL + Spring Boot 서버

Android 앱(`app/`)과 연동하는 **Java + Spring Boot + MySQL** 백엔드입니다.

## 구조

```
TripShare/
├── app/                  # Android 앱
├── database/
│   └── tripshare.sql     # MySQL 테이블 생성 스크립트
└── server/               # Spring Boot API 서버
```

## 1. MySQL 설치 및 DB 생성

MySQL이 실행 중인지 확인한 뒤:

```bash
mysql -u root -p < database/tripshare.sql
```

또는 MySQL Workbench에서 `database/tripshare.sql` 실행

### 생성되는 테이블

| 테이블 | 설명 |
|--------|------|
| `users` | 회원 |
| `trips` | 여행 일정 |
| `schedule_items` | 세부 일정 + 지도 좌표 |
| `reviews` | 후기 |
| `saved_trips` | 저장한 일정 |
| `follows` | 팔로우 |
| `trip_likes` | 좋아요 |

## 2. Spring Boot 설정

`server/src/main/resources/application.yml` 에서 MySQL 접속 정보 수정:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tripshare?...
    username: root
    password: YOUR_PASSWORD   # 본인 MySQL 비밀번호
```

## 3. 서버 실행

```bash
cd server
./gradlew bootRun
```

최초 실행 시 **샘플 데이터**가 자동으로 들어갑니다.

- 테스트 계정: `yuni@example.com` / `1234`

서버 주소: `http://localhost:8080`

## 4. API 목록

| Method | URL | 설명 |
|--------|-----|------|
| POST | `/api/auth/signup` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |
| GET | `/api/trips?query=&userId=` | 일정 목록/검색 |
| GET | `/api/trips/{id}?userId=` | 일정 상세 |
| POST | `/api/trips` | 일정 등록 |
| GET | `/api/trips/{id}/reviews` | 후기 목록 |
| POST | `/api/trips/{id}/reviews` | 후기 작성 |
| POST | `/api/trips/{id}/save?userId=` | 일정 저장 |
| GET | `/api/users/{id}/trips/created` | 내가 만든 일정 |
| GET | `/api/users/{id}/trips/saved` | 저장한 일정 |
| POST | `/api/users/{id}/follow/{targetId}` | 팔로우 |

### 회원가입 예시

```json
POST /api/auth/signup
{
  "email": "test@example.com",
  "password": "1234",
  "nickname": "traveler_테스트"
}
```

### 일정 등록 예시

```json
POST /api/trips
{
  "userId": 1,
  "title": "제주도 2박 3일",
  "destination": "제주도",
  "startDate": "2024.05.01",
  "endDate": "2024.05.03",
  "description": "힐링 여행",
  "scheduleItems": [
    {
      "dayNum": 1,
      "scheduleTime": "09:00",
      "placeName": "제주공항",
      "description": "도착",
      "latitude": 33.5113,
      "longitude": 126.4930
    }
  ]
}
```

## 5. Android 앱 연동 (다음 단계)

앱에서 **Retrofit**으로 위 API를 호출하면 `MockData` 대신 서버 데이터를 사용할 수 있습니다.

- 에뮬레이터: `http://10.0.2.2:8080`
- 실제 기기: PC IP 주소 (예: `http://192.168.0.10:8080`)

## 요구 사항

- Java 17+
- MySQL 8.x
- Gradle (wrapper 포함)
