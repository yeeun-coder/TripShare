# TripShare 🧳

> 여행 일정을 등록하고 다른 사용자와 공유할 수 있는 Android 기반 여행 일정 공유 애플리케이션

## 📌 프로젝트 소개

TripShare는 사용자가 자신의 여행 일정을 등록하고 다른 사용자들과 여행 정보를 공유할 수 있도록 개발한 Android 모바일 애플리케이션입니다.

여행 일정 관리뿐만 아니라 좋아요, 댓글, 일정 저장, 팔로우 기능을 제공하며, Google Maps API를 활용하여 여행 장소를 지도에서 확인할 수 있습니다.

## ✨ 주요 기능

### 1. 회원가입 및 로그인
- 이메일, 비밀번호, 닉네임을 이용한 회원가입
- 로그인 및 사용자 인증
- Google reCAPTCHA를 활용한 CAPTCHA 인증

### 2. 여행 일정 관리
- 여행 일정 등록 및 조회
- 여행 제목, 장소, 여행 기간 및 소개 작성
- 내가 작성한 여행 일정 확인
- 다른 사용자의 여행 일정 열람

### 3. 여행 정보 공유
- 여행 일정 좋아요
- 댓글 및 후기 작성
- 관심 있는 여행 일정 저장
- 다른 사용자 팔로우 및 팔로워 확인

### 4. 지도 기능
- Google Maps API를 활용한 여행지 위치 표시
- 지도 Marker를 통한 여행 장소 확인
- 기기 현재 위치 확인

### 5. 마이페이지
- 프로필 정보 확인 및 수정
- 작성한 일정 및 저장한 일정 확인
- 팔로워 및 팔로잉 목록 확인

## 🛠 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java |
| IDE | Android Studio |
| Backend | Spring Boot |
| Database | MySQL |
| Map API | Google Maps API |
| Authentication | Google reCAPTCHA |
| Version Control | GitHub |

## 📱 주요 화면

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/c31f1df9-fa30-43d1-b560-cac4dd101391" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/bae4f747-a776-4124-8992-4b2c11e92625" />  
  
<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/1c5446bb-9c54-421a-9db0-b0b7e4854304" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/44b7c86a-4e47-44b4-8dfc-12c83efe5572" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/59a6619f-5943-46c6-81b3-8e4996c86166" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/8dc78e84-1679-4ac7-b0e1-6afd5e130f9f" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/aa8ce0a9-bd83-4b1e-af4e-bdd775b7a776" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/1596f287-fb63-4109-bc41-4f1a4806829a" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/e3772f65-fdf5-4e46-8f4e-1f1966109ccf" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/f933e195-07e2-4b81-879c-dbb0e6a0804b" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/22a43c18-3b57-4111-9d13-546e41a69b95" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/90e9130b-6fbf-42fc-ad8a-a2d519edf417" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/bab1dd9b-9a2d-41f4-b417-49bc5b03adea" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/38edd548-7a93-4f6f-b1d9-52cb699117a4" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/2016b590-99aa-4e2d-bdb6-10b563ffe3f9" />

<img width="102" height="227" alt="image" src="https://github.com/user-attachments/assets/3076a81f-1bcf-4948-bb8a-0aeb925a0951" />

<br><br>

| 시작 화면 | 로그인 / 회원가입 |
|---|---|
| 홈 화면 | 일정 상세 화면 |
| 여행 지도 화면 | 일정 등록 화면 |
| 내 일정 화면 | 마이페이지 |

## 📂 프로젝트 구성

```text
TripShare/
├── app/          # Android 애플리케이션
├── server/       # Spring Boot 서버
├── database/     # 데이터베이스 관련 파일
├── gradle/       # Gradle 설정
├── build.gradle
├── settings.gradle
└── README.md
```

## 🚀 실행 방법

1. 저장소를 Clone 합니다.

```bash
git clone https://github.com/yeeun-coder/TripShare.git
```

2. Android Studio에서 프로젝트를 엽니다.
3. 필요한 Gradle 의존성을 설치합니다.
4. 서버와 MySQL 데이터베이스 환경을 설정합니다.
5. Google Maps API 및 reCAPTCHA 관련 설정을 완료합니다.
6. Android 에뮬레이터 또는 기기에서 앱을 실행합니다.

※ 실행에 필요한 API 키와 서버 설정은 별도로 준비해야 합니다.

## 📚 프로젝트를 통해 배운 점

- Android Studio와 Java를 활용한 모바일 애플리케이션 개발
- Spring Boot 서버와 Android 애플리케이션 연동
- MySQL 데이터베이스 설계 및 데이터 관리
- 외부 API를 활용한 지도 및 사용자 인증 기능 구현
- 사용자 중심의 UI 설계와 주요 기능 테스트

## 📅 프로젝트 정보

- 프로젝트명: TripShare
- 개발 분야: Android 모바일 애플리케이션
- 개발 기간: 2026.05.01 ~ 2026.06.20
- 개발 유형: 모바일 프로그래밍 프로젝트
