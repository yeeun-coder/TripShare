package com.inhatc.tripshare.api;

import com.inhatc.tripshare.R;
import com.inhatc.tripshare.api.dto.ReviewDto;
import com.inhatc.tripshare.api.dto.ScheduleItemDto;
import com.inhatc.tripshare.api.dto.TripDto;
import com.inhatc.tripshare.model.Review;
import com.inhatc.tripshare.model.ScheduleItem;
import com.inhatc.tripshare.model.Trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class ApiMapper {

    private static final int[] TRIP_IMAGES = {
            R.drawable.bg_trip_1,
            R.drawable.bg_trip_2,
            R.drawable.bg_trip_3,
            R.drawable.bg_trip_4
    };

    private ApiMapper() {
    }

    public static Trip toTrip(TripDto dto) {
        List<ScheduleItem> schedule = new ArrayList<>();
        if (dto.scheduleItems != null) {
            for (ScheduleItemDto item : dto.scheduleItems) {
                schedule.add(new ScheduleItem(
                        item.dayNum,
                        item.scheduleTime,
                        item.placeName,
                        item.description != null ? item.description : "",
                        item.latitude,
                        item.longitude));
            }
        }

        long authorUserId = dto.authorUserId != null ? dto.authorUserId : 0;
        int id = dto.id != null ? dto.id.intValue() : 0;
        int imageRes = TRIP_IMAGES[Math.abs(id) % TRIP_IMAGES.length];

        return new Trip(
                id,
                authorUserId,
                dto.title != null ? dto.title : "",
                dto.author != null ? dto.author : "",
                dto.authorProfileImage,
                dto.destination != null ? dto.destination : "",
                dto.startDate != null ? dto.startDate : "",
                dto.endDate != null ? dto.endDate : "",
                dto.description != null ? dto.description : "",
                dto.coverUrl,
                imageRes,
                dto.likeCount,
                dto.reviewCount,
                schedule,
                dto.mine,
                dto.saved,
                dto.liked,
                dto.followingAuthor);
    }

    public static List<Trip> toTrips(List<TripDto> dtos) {
        List<Trip> trips = new ArrayList<>();
        if (dtos == null) {
            return trips;
        }
        for (TripDto dto : dtos) {
            trips.add(toTrip(dto));
        }
        return trips;
    }

    public static List<Trip> toPopularTrips(List<TripDto> dtos) {
        List<Trip> trips = toTripsSortedByLikes(dtos);
        return trips.subList(0, Math.min(3, trips.size()));
    }

    public static List<Trip> toTripsSortedByLikes(List<TripDto> dtos) {
        List<Trip> trips = toTrips(dtos);
        trips.sort(Comparator.comparingInt(Trip::getLikes).reversed());
        return trips;
    }

    public static Review toReview(ReviewDto dto, int tripId) {
        return new Review(
                dto.id != null ? dto.id.intValue() : 0,
                tripId,
                dto.author != null ? dto.author : "",
                dto.content != null ? dto.content : "",
                dto.createdAt != null ? dto.createdAt : "",
                dto.rating);
    }

    public static List<Review> toReviews(List<ReviewDto> dtos, int tripId) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        List<Review> reviews = new ArrayList<>();
        for (ReviewDto dto : dtos) {
            reviews.add(toReview(dto, tripId));
        }
        return reviews;
    }
}
