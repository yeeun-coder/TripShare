-- TripMate MySQL Database
-- 실행: mysql -u root -p < database/tripshare.sql

CREATE DATABASE IF NOT EXISTS tripshare
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE tripshare;

-- 회원
CREATE TABLE users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL COMMENT 'BCrypt 해시',
    nickname    VARCHAR(50)  NOT NULL,
    bio         VARCHAR(255) DEFAULT NULL,
    profile_image MEDIUMTEXT DEFAULT NULL COMMENT 'Base64 JPEG',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 여행 일정
CREATE TABLE trips (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    title        VARCHAR(200) NOT NULL,
    destination  VARCHAR(100) NOT NULL,
    start_date   DATE         NOT NULL,
    end_date     DATE         NOT NULL,
    description  TEXT,
    cover_url    MEDIUMTEXT DEFAULT NULL,
    like_count   INT          NOT NULL DEFAULT 0,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trips_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_trips_destination (destination),
    INDEX idx_trips_created_at (created_at)
) ENGINE=InnoDB;

-- 세부 일정 (지도 마커 좌표 포함)
CREATE TABLE schedule_items (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    trip_id      BIGINT        NOT NULL,
    day_num      INT           NOT NULL,
    schedule_time VARCHAR(10)  NOT NULL COMMENT '예: 09:00',
    place_name   VARCHAR(200)  NOT NULL,
    description  VARCHAR(500)  DEFAULT NULL,
    latitude     DECIMAL(10,7) NOT NULL,
    longitude    DECIMAL(10,7) NOT NULL,
    sort_order   INT           NOT NULL DEFAULT 0,
    CONSTRAINT fk_schedule_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
    INDEX idx_schedule_trip (trip_id)
) ENGINE=InnoDB;

-- 후기
CREATE TABLE reviews (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    trip_id    BIGINT       NOT NULL,
    user_id    BIGINT       NOT NULL,
    content    TEXT         NOT NULL,
    rating     TINYINT      NOT NULL CHECK (rating BETWEEN 1 AND 5),
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_reviews_trip (trip_id)
) ENGINE=InnoDB;

-- 저장한 일정
CREATE TABLE saved_trips (
    user_id    BIGINT   NOT NULL,
    trip_id    BIGINT   NOT NULL,
    saved_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, trip_id),
    CONSTRAINT fk_saved_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_saved_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 팔로우
CREATE TABLE follows (
    follower_id  BIGINT   NOT NULL COMMENT '팔로우 하는 사람',
    following_id BIGINT   NOT NULL COMMENT '팔로우 대상',
    created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, following_id),
    CONSTRAINT fk_follow_follower  FOREIGN KEY (follower_id)  REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_follow_following FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 좋아요
CREATE TABLE trip_likes (
    user_id    BIGINT   NOT NULL,
    trip_id    BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, trip_id),
    CONSTRAINT fk_like_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_like_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE CASCADE
) ENGINE=InnoDB;
