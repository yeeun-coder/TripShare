package com.inhatc.tripshare.controller;

import com.inhatc.tripshare.dto.*;
import com.inhatc.tripshare.service.TripShareService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripShareService service;

    public TripController(TripShareService service) {
        this.service = service;
    }

    @GetMapping
    public List<TripResponse> list(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long userId) {
        return service.getTrips(query, userId);
    }

    @GetMapping("/{tripId}")
    public TripResponse detail(@PathVariable Long tripId,
                               @RequestParam(required = false) Long userId) {
        return service.getTrip(tripId, userId);
    }

    @PostMapping
    public TripResponse create(@Valid @RequestBody TripCreateRequest request) {
        return service.createTrip(request);
    }

    @PutMapping("/{tripId}")
    public TripResponse update(@PathVariable Long tripId,
                               @RequestParam Long userId,
                               @Valid @RequestBody TripUpdateRequest request) {
        return service.updateTrip(tripId, userId, request);
    }

    @GetMapping("/{tripId}/reviews")
    public List<ReviewResponse> reviews(@PathVariable Long tripId) {
        return service.getReviews(tripId);
    }

    @PostMapping("/{tripId}/reviews")
    public ReviewResponse addReview(@PathVariable Long tripId,
                                    @Valid @RequestBody ReviewCreateRequest request) {
        return service.addReview(tripId, request);
    }

    @PostMapping("/{tripId}/save")
    public Map<String, String> save(@PathVariable Long tripId, @RequestParam Long userId) {
        service.saveTrip(userId, tripId);
        return Map.of("message", "saved");
    }

    @DeleteMapping("/{tripId}/save")
    public Map<String, String> unsave(@PathVariable Long tripId, @RequestParam Long userId) {
        service.unsaveTrip(userId, tripId);
        return Map.of("message", "unsaved");
    }

    @PostMapping("/{tripId}/like")
    public Map<String, String> like(@PathVariable Long tripId, @RequestParam Long userId) {
        service.likeTrip(userId, tripId);
        return Map.of("message", "liked");
    }

    @DeleteMapping("/{tripId}/like")
    public Map<String, String> unlike(@PathVariable Long tripId, @RequestParam Long userId) {
        service.unlikeTrip(userId, tripId);
        return Map.of("message", "unliked");
    }
}
