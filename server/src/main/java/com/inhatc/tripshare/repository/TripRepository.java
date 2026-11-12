package com.inhatc.tripshare.repository;

import com.inhatc.tripshare.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findAllByOrderByCreatedAtDesc();

    List<Trip> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT t FROM Trip t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(t.destination) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(t.user.nickname) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "ORDER BY t.createdAt DESC")
    List<Trip> search(@Param("q") String query);
}
