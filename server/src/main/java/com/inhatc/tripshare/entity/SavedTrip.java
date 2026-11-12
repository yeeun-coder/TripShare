package com.inhatc.tripshare.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "saved_trips")
@IdClass(SavedTrip.SavedTripId.class)
public class SavedTrip {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt = LocalDateTime.now();

    public SavedTrip() {
    }

    public SavedTrip(Long userId, Long tripId) {
        this.userId = userId;
        this.tripId = tripId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTripId() {
        return tripId;
    }

    public static class SavedTripId implements Serializable {
        private Long userId;
        private Long tripId;

        public SavedTripId() {
        }

        public SavedTripId(Long userId, Long tripId) {
            this.userId = userId;
            this.tripId = tripId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SavedTripId that)) return false;
            return Objects.equals(userId, that.userId) && Objects.equals(tripId, that.tripId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, tripId);
        }
    }
}
