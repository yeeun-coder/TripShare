package com.inhatc.tripshare.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "trip_likes")
@IdClass(TripLike.TripLikeId.class)
public class TripLike {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public TripLike() {
    }

    public TripLike(Long userId, Long tripId) {
        this.userId = userId;
        this.tripId = tripId;
    }

    public static class TripLikeId implements Serializable {
        private Long userId;
        private Long tripId;

        public TripLikeId() {
        }

        public TripLikeId(Long userId, Long tripId) {
            this.userId = userId;
            this.tripId = tripId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TripLikeId that)) return false;
            return Objects.equals(userId, that.userId)
                    && Objects.equals(tripId, that.tripId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, tripId);
        }
    }
}
