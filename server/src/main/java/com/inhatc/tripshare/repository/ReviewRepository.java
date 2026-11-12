package com.inhatc.tripshare.repository;

import com.inhatc.tripshare.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTripIdOrderByCreatedAtDesc(Long tripId);
    int countByTripId(Long tripId);
}
