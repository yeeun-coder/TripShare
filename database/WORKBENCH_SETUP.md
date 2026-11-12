# MySQL Workbench 첫 접속 가이드

## 사전 준비

터미널에서 **한 번만** 실행 (Mac 비밀번호 입력 필요):

```bash
cd /Users/pcfriend/Desktop/dev/TripShare
bash database/setup-mysql-mac.sh
```

이 스크립트가 자동으로:
- MySQL root 비밀번호 → `tripshare1234` 설정
- `tripshare` DB 생성
- Spring `application-local.yml` 업데이트

---

## Workbench 수동 접속 (GUI)

스크립트 없이 직접 하려면:

### 1. Workbench 실행

`/Applications/MySQLWorkbench.app` 더블클릭

### 2. 연결 만들기

왼쪽 **MySQL Connections** 옆 `+` 클릭

| 항목 | 값 |
|------|-----|
| Connection Name | TripShare Local |
| Hostname | 127.0.0.1 |
| Port | 3306 |
| Username | root |
| Password | Store in Keychain → `tripshare1234` |

**Test Connection** → Success → OK

> 스크립트 실행 시 `TripShare Local` 연결이 이미 생성되어 있을 수 있습니다.

### 3. DB 생성 (스크립트 미실행 시)

1. 연결 더블클릭
2. **File → Open SQL Script...**
3. `database/tripshare.sql` 선택
4. 번개 아이콘(Execute) 클릭
5. Schemas에 `tripshare` 확인

### 4. Spring 서버 실행

```bash
cd server
./gradlew bootRun
```

### 5. 앱 테스트

에뮬레이터에서 `yuni@example.com` / `1234` 로그인
