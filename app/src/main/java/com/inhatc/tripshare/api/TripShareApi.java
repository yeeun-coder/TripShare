package com.inhatc.tripshare.api;

import com.inhatc.tripshare.api.dto.LoginRequest;
import com.inhatc.tripshare.api.dto.ReviewCreateRequest;
import com.inhatc.tripshare.api.dto.ReviewDto;
import com.inhatc.tripshare.api.dto.SignUpRequest;
import com.inhatc.tripshare.api.dto.TripCreateRequest;
import com.inhatc.tripshare.api.dto.TripDto;
import com.inhatc.tripshare.api.dto.TripUpdateRequest;
import com.inhatc.tripshare.api.dto.UserDto;
import com.inhatc.tripshare.api.dto.UserSummaryDto;
import com.inhatc.tripshare.api.dto.UserUpdateRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TripShareApi {

    @POST("api/auth/signup")
    Call<UserDto> signUp(@Body SignUpRequest request);

    @POST("api/auth/login")
    Call<UserDto> login(@Body LoginRequest request);

    @GET("api/trips")
    Call<List<TripDto>> getTrips(@Query("query") String query,
                                 @Query("userId") Long userId);

    @GET("api/trips/{tripId}")
    Call<TripDto> getTrip(@Path("tripId") long tripId,
                          @Query("userId") Long userId);

    @POST("api/trips")
    Call<TripDto> createTrip(@Body TripCreateRequest request);

    @PUT("api/trips/{tripId}")
    Call<TripDto> updateTrip(@Path("tripId") long tripId,
                             @Query("userId") long userId,
                             @Body TripUpdateRequest request);

    @GET("api/trips/{tripId}/reviews")
    Call<List<ReviewDto>> getReviews(@Path("tripId") long tripId);

    @POST("api/trips/{tripId}/reviews")
    Call<ReviewDto> addReview(@Path("tripId") long tripId,
                              @Body ReviewCreateRequest request);

    @POST("api/trips/{tripId}/save")
    Call<Map<String, String>> saveTrip(@Path("tripId") long tripId,
                                       @Query("userId") long userId);

    @DELETE("api/trips/{tripId}/save")
    Call<Map<String, String>> unsaveTrip(@Path("tripId") long tripId,
                                         @Query("userId") long userId);

    @POST("api/trips/{tripId}/like")
    Call<Map<String, String>> likeTrip(@Path("tripId") long tripId,
                                       @Query("userId") long userId);

    @DELETE("api/trips/{tripId}/like")
    Call<Map<String, String>> unlikeTrip(@Path("tripId") long tripId,
                                         @Query("userId") long userId);

    @GET("api/users/{userId}")
    Call<UserDto> getUser(@Path("userId") long userId);

    @PUT("api/users/{userId}")
    Call<UserDto> updateUser(@Path("userId") long userId,
                             @Body UserUpdateRequest request);

    @GET("api/users/{userId}/trips/created")
    Call<List<TripDto>> getCreatedTrips(@Path("userId") long userId);

    @GET("api/users/{userId}/trips/saved")
    Call<List<TripDto>> getSavedTrips(@Path("userId") long userId);

    @POST("api/users/{userId}/follow/{targetId}")
    Call<Map<String, String>> follow(@Path("userId") long userId,
                                     @Path("targetId") long targetId);

    @GET("api/users/{userId}/followers")
    Call<List<UserSummaryDto>> getFollowers(@Path("userId") long userId);

    @GET("api/users/{userId}/following")
    Call<List<UserSummaryDto>> getFollowing(@Path("userId") long userId);
}
