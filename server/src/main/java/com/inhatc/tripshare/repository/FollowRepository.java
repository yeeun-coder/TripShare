package com.inhatc.tripshare.repository;

import com.inhatc.tripshare.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Follow.FollowId> {
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    long countByFollowingId(Long followingId);

    long countByFollowerId(Long followerId);

    java.util.List<Follow> findByFollowingIdOrderByCreatedAtDesc(Long followingId);

    java.util.List<Follow> findByFollowerIdOrderByCreatedAtDesc(Long followerId);
}
