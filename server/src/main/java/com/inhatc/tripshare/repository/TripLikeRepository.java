package com.inhatc.tripshare.repository;

import com.inhatc.tripshare.entity.TripLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripLikeRepository extends JpaRepository<TripLike, TripLike.TripLikeId> {
    boolean existsByUserIdAndTripId(Long userId, Long tripId);

    void deleteByUserIdAndTripId(Long userId, Long tripId);
}
