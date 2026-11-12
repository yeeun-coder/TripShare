-- 기존 DB에 profile_image 컬럼 추가 (최초 tripshare.sql 이후 한 번 실행)
USE tripshare;
ALTER TABLE users ADD COLUMN profile_image MEDIUMTEXT DEFAULT NULL COMMENT 'Base64 JPEG';
