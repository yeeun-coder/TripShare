package com.inhatc.tripshare.repository;

import com.inhatc.tripshare.entity.SavedTrip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedTripRepository extends JpaRepository<SavedTrip, SavedTrip.SavedTripId> {
    List<SavedTrip> findByUserIdOrderBySavedAtDesc(Long userId);
    boolean existsByUserIdAndTripId(Long userId, Long tripId);
    void deleteByUserIdAndTripId(Long userId, Long tripId);
}
