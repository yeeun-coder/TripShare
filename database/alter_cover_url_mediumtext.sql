-- 기존 DB: 커버 이미지 Base64 저장을 위해 컬럼 확장
ALTER TABLE trips MODIFY COLUMN cover_url MEDIUMTEXT;
