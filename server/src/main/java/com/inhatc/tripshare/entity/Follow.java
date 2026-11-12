package com.inhatc.tripshare.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "follows")
@IdClass(Follow.FollowId.class)
public class Follow {

    @Id
    @Column(name = "follower_id")
    private Long followerId;

    @Id
    @Column(name = "following_id")
    private Long followingId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Follow() {
    }

    public Follow(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public static class FollowId implements Serializable {
        private Long followerId;
        private Long followingId;

        public FollowId() {
        }

        public FollowId(Long followerId, Long followingId) {
            this.followerId = followerId;
            this.followingId = followingId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof FollowId that)) return false;
            return Objects.equals(followerId, that.followerId)
                    && Objects.equals(followingId, that.followingId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(followerId, followingId);
        }
    }
}
