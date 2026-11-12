#!/bin/bash
# MySQL 초기 설정 스크립트 (Mac 공식 설치본)
# 터미널에서 실행: bash database/setup-mysql-mac.sh
# Mac 로그인 비밀번호(sudo) 입력이 필요합니다.

set -e

MYSQL="/usr/local/mysql/bin/mysql"
MYSQLADMIN="/usr/local/mysql/bin/mysqladmin"
MYSQL_SERVER="/usr/local/mysql/support-files/mysql.server"
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
INIT_SQL="$PROJECT_ROOT/database/mysql-init-password.sql"
TRIPSHARE_SQL="$PROJECT_ROOT/database/tripshare.sql"
LOCAL_YML="$PROJECT_ROOT/server/src/main/resources/application-local.yml"
ROOT_PASSWORD="tripshare1234"

echo "=== TripShare MySQL 설정 ==="

if [ ! -x "$MYSQL" ]; then
  echo "MySQL이 /usr/local/mysql 에 없습니다."
  echo "https://dev.mysql.com/downloads/mysql/ 에서 설치 후 다시 실행하세요."
  exit 1
fi

echo "[1/5] MySQL root 비밀번호를 '${ROOT_PASSWORD}' 로 설정합니다..."

# 이미 설정된 비밀번호로 접속 가능한지 확인
if $MYSQL -u root -p"${ROOT_PASSWORD}" -e "SELECT 1;" &>/dev/null; then
  echo "  -> 이미 root 비밀번호가 '${ROOT_PASSWORD}' 입니다. 건너뜁니다."
else
  echo "  -> MySQL을 잠시 중지하고 비밀번호를 재설정합니다 (sudo 필요)..."
  sudo "$MYSQL_SERVER" stop 2>/dev/null || true
  sleep 3

  # mysqld --init-file 로 root 비밀번호 설정 (MySQL 8+)
  sudo "$MYSQL_SERVER" start --init-file="$INIT_SQL" 2>/dev/null || {
    sudo /usr/local/mysql/bin/mysqld --user=_mysql --init-file="$INIT_SQL" &
    sleep 8
    sudo killall mysqld 2>/dev/null || true
    sleep 3
  }

  sudo "$MYSQL_SERVER" start
  sleep 4

  if ! $MYSQL -u root -p"${ROOT_PASSWORD}" -e "SELECT 1;" &>/dev/null; then
    echo "  !! 비밀번호 설정 실패. 시스템 설정 > MySQL 에서 Initialize Database 를 시도하거나"
    echo "     MySQL Workbench 로 root 비밀번호를 직접 설정한 뒤 application-local.yml 에 입력하세요."
    exit 1
  fi
  echo "  -> root 비밀번호 설정 완료"
fi

echo "[2/5] tripshare 데이터베이스 생성..."
$MYSQL -u root -p"${ROOT_PASSWORD}" < "$TRIPSHARE_SQL"
echo "  -> DB/테이블 생성 완료"

echo "[3/5] application-local.yml 업데이트..."
cat > "$LOCAL_YML" << EOF
# 로컬 MySQL 설정 (git 제외)
# root 비밀번호: ${ROOT_PASSWORD}

spring:
  datasource:
    password: ${ROOT_PASSWORD}
EOF
echo "  -> $LOCAL_YML 저장 완료"

echo "[4/5] MySQL Workbench 연결 파일 생성..."
WB_DIR="$HOME/Library/Application Support/MySQL/Workbench"
mkdir -p "$WB_DIR"
cat > "$WB_DIR/connections.xml" << 'XMLEOF'
<?xml version="1.0"?>
<data grt_format="2.0">
  <value type="list" content-type="object" content-struct-name="db.mgmt.Connection">
    <value type="object" struct-name="db.mgmt.Connection" id="tripshare-local">
      <value type="string" key="name">TripShare Local</value>
      <value type="string" key="hostName">127.0.0.1</value>
      <value type="int" key="port">3306</value>
      <value type="string" key="userName">root</value>
      <value type="string" key="schema"></value>
      <value type="string" key="driver">Native</value>
    </value>
  </value>
</data>
XMLEOF
echo "  -> Workbench 연결 'TripShare Local' 생성 (비밀번호: ${ROOT_PASSWORD})"

echo "[5/5] Spring 서버 시작 테스트..."
cd "$PROJECT_ROOT/server"
./gradlew bootRun --quiet &
SERVER_PID=$!
sleep 15

if curl -sf http://localhost:8080/api/trips >/dev/null; then
  echo "  -> API 응답 OK: http://localhost:8080/api/trips"
  kill $SERVER_PID 2>/dev/null || true
else
  echo "  -> 서버 시작 확인 필요: cd server && ./gradlew bootRun"
  kill $SERVER_PID 2>/dev/null || true
fi

echo ""
echo "=== 설정 완료 ==="
echo "MySQL root 비밀번호 : ${ROOT_PASSWORD}"
echo "Workbench           : TripShare Local (127.0.0.1:3306)"
echo "Spring 서버         : cd server && ./gradlew bootRun"
echo "앱 테스트 계정      : yuni@example.com / 1234"
echo ""
