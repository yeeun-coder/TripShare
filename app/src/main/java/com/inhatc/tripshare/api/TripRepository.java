package com.inhatc.tripshare.api;

import com.inhatc.tripshare.api.dto.LoginRequest;
import com.inhatc.tripshare.api.dto.ReviewCreateRequest;
import com.inhatc.tripshare.api.dto.SignUpRequest;
import com.inhatc.tripshare.api.dto.TripCreateRequest;
import com.inhatc.tripshare.api.dto.TripUpdateRequest;
import com.inhatc.tripshare.api.dto.UserDto;
import com.inhatc.tripshare.api.dto.UserUpdateRequest;
import com.inhatc.tripshare.model.Review;
import com.inhatc.tripshare.model.Trip;
import com.inhatc.tripshare.model.UserProfile;

import java.util.List;

public final class TripRepository {

    private static final TripRepository INSTANCE = new TripRepository();
    private final TripShareApi api = ApiClient.getApi();

    private TripRepository() {
    }

    public static TripRepository getInstance() {
        return INSTANCE;
    }

    public void login(String email, String password, ApiCallback<UserDto> callback) {
        ApiHelper.enqueue(api.login(new LoginRequest(email, password)), new ApiCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto data) {
                SessionManager.setCurrentUser(data);
                callback.onSuccess(data);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void signUp(String email, String password, String nickname,
                       ApiCallback<UserDto> callback) {
        ApiHelper.enqueue(api.signUp(new SignUpRequest(email, password, nickname)),
                new ApiCallback<UserDto>() {
                    @Override
                    public void onSuccess(UserDto data) {
                        SessionManager.setCurrentUser(data);
                        callback.onSuccess(data);
                    }

                    @Override
                    public void onError(String message) {
                        callback.onError(message);
                    }
                });
    }

    public void getLatestTrips(ApiCallback<List<Trip>> callback) {
        ApiHelper.enqueue(api.getTrips(null, SessionManager.getUserId()), forward(callback, ApiMapper::toTrips));
    }

    public void getPopularTrips(ApiCallback<List<Trip>> callback) {
        ApiHelper.enqueue(api.getTrips(null, SessionManager.getUserId()), forward(callback, ApiMapper::toPopularTrips));
    }

    public void searchTrips(String query, ApiCallback<List<Trip>> callback) {
        String q = query == null || query.trim().isEmpty() ? null : query.trim();
        ApiHelper.enqueue(api.getTrips(q, SessionManager.getUserId()), forward(callback, ApiMapper::toTrips));
    }

    public void getTripsSortedByLikes(ApiCallback<List<Trip>> callback) {
        ApiHelper.enqueue(api.getTrips(null, SessionManager.getUserId()),
                forward(callback, ApiMapper::toTripsSortedByLikes));
    }

    public void getTrip(long tripId, ApiCallback<Trip> callback) {
        ApiHelper.enqueue(api.getTrip(tripId, SessionManager.getUserId()),
                forward(callback, ApiMapper::toTrip));
    }

    public void getMyCreatedTrips(ApiCallback<List<Trip>> callback) {
        getCreatedTrips(SessionManager.getUserId(), callback);
    }

    public void getCreatedTrips(long userId, ApiCallback<List<Trip>> callback) {
        ApiHelper.enqueue(api.getCreatedTrips(userId),
                forward(callback, ApiMapper::toTrips));
    }

    public void getSavedTrips(ApiCallback<List<Trip>> callback) {
        ApiHelper.enqueue(api.getSavedTrips(SessionManager.getUserId()),
                forward(callback, ApiMapper::toTrips));
    }

    public void createTrip(TripCreateRequest request, ApiCallback<Trip> callback) {
        ApiHelper.enqueue(api.createTrip(request), forward(callback, ApiMapper::toTrip));
    }

    public void updateTrip(long tripId, TripUpdateRequest request, ApiCallback<Trip> callback) {
        ApiHelper.enqueue(api.updateTrip(tripId, SessionManager.getUserId(), request),
                forward(callback, ApiMapper::toTrip));
    }

    public void getReviews(long tripId, ApiCallback<List<Review>> callback) {
        ApiHelper.enqueue(api.getReviews(tripId), new ApiCallback<List<com.inhatc.tripshare.api.dto.ReviewDto>>() {
            @Override
            public void onSuccess(List<com.inhatc.tripshare.api.dto.ReviewDto> data) {
                callback.onSuccess(ApiMapper.toReviews(data, (int) tripId));
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void addReview(long tripId, String content, int rating, ApiCallback<Review> callback) {
        ReviewCreateRequest request = new ReviewCreateRequest(
                SessionManager.getUserId(), content, rating);
        ApiHelper.enqueue(api.addReview(tripId, request), new ApiCallback<com.inhatc.tripshare.api.dto.ReviewDto>() {
            @Override
            public void onSuccess(com.inhatc.tripshare.api.dto.ReviewDto data) {
                callback.onSuccess(ApiMapper.toReview(data, (int) tripId));
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void saveTrip(long tripId, ApiCallback<Void> callback) {
        ApiHelper.enqueue(api.saveTrip(tripId, SessionManager.getUserId()), new ApiCallback<java.util.Map<String, String>>() {
            @Override
            public void onSuccess(java.util.Map<String, String> data) {
                callback.onSuccess(null);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void unsaveTrip(long tripId, ApiCallback<Void> callback) {
        ApiHelper.enqueue(api.unsaveTrip(tripId, SessionManager.getUserId()), new ApiCallback<java.util.Map<String, String>>() {
            @Override
            public void onSuccess(java.util.Map<String, String> data) {
                callback.onSuccess(null);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void likeTrip(long tripId, ApiCallback<Void> callback) {
        ApiHelper.enqueue(api.likeTrip(tripId, SessionManager.getUserId()),
                mapVoidCallback(callback));
    }

    public void unlikeTrip(long tripId, ApiCallback<Void> callback) {
        ApiHelper.enqueue(api.unlikeTrip(tripId, SessionManager.getUserId()),
                mapVoidCallback(callback));
    }

    public void getFollowers(long userId, ApiCallback<List<com.inhatc.tripshare.api.dto.UserSummaryDto>> callback) {
        ApiHelper.enqueue(api.getFollowers(userId), callback);
    }

    public void getFollowing(long userId, ApiCallback<List<com.inhatc.tripshare.api.dto.UserSummaryDto>> callback) {
        ApiHelper.enqueue(api.getFollowing(userId), callback);
    }

    public void getUserProfile(long userId, ApiCallback<UserProfile> callback) {
        ApiHelper.enqueue(api.getUser(userId), new ApiCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto data) {
                callback.onSuccess(new UserProfile(
                        data.nickname,
                        data.email,
                        data.bio,
                        0,
                        0,
                        0,
                        data.followerCount,
                        data.followingCount));
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    public void getUser(long userId, ApiCallback<com.inhatc.tripshare.api.dto.UserDto> callback) {
        ApiHelper.enqueue(api.getUser(userId), callback);
    }

    public void updateUser(long userId, UserUpdateRequest request,
                           ApiCallback<com.inhatc.tripshare.api.dto.UserDto> callback) {
        ApiHelper.enqueue(api.updateUser(userId, request), callback);
    }

    public void followUser(long targetUserId, ApiCallback<Void> callback) {
        ApiHelper.enqueue(api.follow(SessionManager.getUserId(), targetUserId),
                new ApiCallback<java.util.Map<String, String>>() {
                    @Override
                    public void onSuccess(java.util.Map<String, String> data) {
                        callback.onSuccess(null);
                    }

                    @Override
                    public void onError(String message) {
                        callback.onError(message);
                    }
                });
    }

    public void loadProfileStats(ApiCallback<UserProfile> callback) {
        long userId = SessionManager.getUserId();
        ApiHelper.enqueue(api.getUser(userId), new ApiCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto userDto) {
                loadTripStats(userId, userDto, callback);
            }

            @Override
            public void onError(String message) {
                loadTripStats(userId, null, callback);
            }
        });
    }

    private void loadTripStats(long userId, UserDto userDto, ApiCallback<UserProfile> callback) {
        ApiHelper.enqueue(api.getCreatedTrips(userId), new ApiCallback<List<com.inhatc.tripshare.api.dto.TripDto>>() {
            @Override
            public void onSuccess(List<com.inhatc.tripshare.api.dto.TripDto> created) {
                List<Trip> createdTrips = ApiMapper.toTrips(created);
                int totalLikes = 0;
                for (Trip trip : createdTrips) {
                    totalLikes += trip.getLikes();
                }
                final int createdCount = createdTrips.size();
                final int likesReceived = totalLikes;
                ApiHelper.enqueue(api.getSavedTrips(userId),
                        new ApiCallback<List<com.inhatc.tripshare.api.dto.TripDto>>() {
                            @Override
                            public void onSuccess(List<com.inhatc.tripshare.api.dto.TripDto> saved) {
                                UserProfile base = SessionManager.getProfile();
                                long followers = userDto != null ? userDto.followerCount : base.getFollowerCount();
                                long following = userDto != null ? userDto.followingCount : base.getFollowingCount();
                                callback.onSuccess(new UserProfile(
                                        base.getNickname(),
                                        base.getEmail(),
                                        base.getBio(),
                                        createdCount,
                                        ApiMapper.toTrips(saved).size(),
                                        likesReceived,
                                        followers,
                                        following));
                            }

                            @Override
                            public void onError(String message) {
                                callback.onError(message);
                            }
                        });
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }

    private interface Mapper<T, R> {
        R map(T input);
    }

    private ApiCallback<java.util.Map<String, String>> mapVoidCallback(ApiCallback<Void> callback) {
        return new ApiCallback<java.util.Map<String, String>>() {
            @Override
            public void onSuccess(java.util.Map<String, String> data) {
                callback.onSuccess(null);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        };
    }

    private <T, R> ApiCallback<T> forward(ApiCallback<R> callback, Mapper<T, R> mapper) {
        return new ApiCallback<T>() {
            @Override
            public void onSuccess(T data) {
                callback.onSuccess(mapper.map(data));
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        };
    }
}
