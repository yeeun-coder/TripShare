package com.inhatc.tripshare.controller;

import com.inhatc.tripshare.dto.*;
import com.inhatc.tripshare.service.TripShareService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final TripShareService service;

    public UserController(TripShareService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public UserResponse profile(@PathVariable Long userId) {
        return service.getUser(userId);
    }

    @PutMapping("/{userId}")
    public UserResponse updateProfile(@PathVariable Long userId,
                                      @RequestBody UserUpdateRequest request) {
        return service.updateUser(userId, request);
    }

    @GetMapping("/{userId}/trips/created")
    public List<TripResponse> createdTrips(@PathVariable Long userId) {
        return service.getMyTrips(userId);
    }

    @GetMapping("/{userId}/trips/saved")
    public List<TripResponse> savedTrips(@PathVariable Long userId) {
        return service.getSavedTrips(userId);
    }

    @PostMapping("/{userId}/follow/{targetId}")
    public Map<String, String> follow(@PathVariable Long userId, @PathVariable Long targetId) {
        service.follow(userId, targetId);
        return Map.of("message", "followed");
    }

    @GetMapping("/{userId}/followers")
    public List<UserSummaryResponse> followers(@PathVariable Long userId) {
        return service.getFollowers(userId);
    }

    @GetMapping("/{userId}/following")
    public List<UserSummaryResponse> following(@PathVariable Long userId) {
        return service.getFollowing(userId);
    }
}
