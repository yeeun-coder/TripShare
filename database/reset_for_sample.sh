#!/bin/bash
# 샘플 데이터를 새로 넣으려면 이 스크립트 실행 후 서버 재시작
# 사용: bash database/reset_for_sample.sh

set -e
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
MYSQL="${MYSQL:-mysql}"
PASS="${MYSQL_PASSWORD:-tripshare1234}"
HOST="${MYSQL_HOST:-127.0.0.1}"
PORT="${MYSQL_PORT:-3307}"

echo "=== TripShare DB 초기화 ==="
echo "MySQL: $HOST:$PORT"

$MYSQL -u root -p"$PASS" -h "$HOST" -P "$PORT" -e "DROP DATABASE IF EXISTS tripshare;"
$MYSQL -u root -p"$PASS" -h "$HOST" -P "$PORT" < "$ROOT/database/tripshare.sql"

echo ""
echo "완료! 이제 서버를 재시작하세요:"
echo "  cd server && ./gradlew bootRun"
echo ""
echo "테스트 계정 (비밀번호 1234):"
echo "  yuni@example.com      - traveler_유니 (메인 테스트)"
echo "  foodie@example.com    - foodie_민수"
echo "  culture@example.com   - culture_지은"
echo "  ocean@example.com     - ocean_하늘"
echo "  camper@example.com    - camper_수"
echo "  photo@example.com     - photo_다희"
