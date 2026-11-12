package com.inhatc.tripshare.service;

import com.inhatc.tripshare.dto.*;
import com.inhatc.tripshare.entity.*;
import com.inhatc.tripshare.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TripShareService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final DateTimeFormatter REVIEW_DATE_FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final ReviewRepository reviewRepository;
    private final SavedTripRepository savedTripRepository;
    private final FollowRepository followRepository;
    private final TripLikeRepository tripLikeRepository;
    private final PasswordEncoder passwordEncoder;

    public TripShareService(UserRepository userRepository,
                            TripRepository tripRepository,
                            ReviewRepository reviewRepository,
                            SavedTripRepository savedTripRepository,
                            FollowRepository followRepository,
                            TripLikeRepository tripLikeRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.reviewRepository = reviewRepository;
        this.savedTripRepository = savedTripRepository;
        this.followRepository = followRepository;
        this.tripLikeRepository = tripLikeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setBio("여행을 사랑하는 " + request.getNickname() + "입니다 ✈️");
        userRepository.save(user);
        return toUserResponse(user);
    }

    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 실패"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 실패");
        }
        return toUserResponse(user);
    }

    public UserResponse getUser(Long userId) {
        User user = findUser(userId);
        return toUserResponse(user);
    }

    public List<TripResponse> getTrips(String query, Long currentUserId) {
        List<Trip> trips = (query == null || query.isBlank())
                ? tripRepository.findAllByOrderByCreatedAtDesc()
                : tripRepository.search(query.trim());
        return trips.stream()
                .map(t -> toTripResponse(t, currentUserId, true))
                .collect(Collectors.toList());
    }

    public TripResponse getTrip(Long tripId, Long currentUserId) {
        Trip trip = findTrip(tripId);
        return toTripResponse(trip, currentUserId, true);
    }

    public List<TripResponse> getMyTrips(Long userId) {
        return tripRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(t -> toTripResponse(t, userId, true))
                .collect(Collectors.toList());
    }

    public List<TripResponse> getSavedTrips(Long userId) {
        return savedTripRepository.findByUserIdOrderBySavedAtDesc(userId).stream()
                .map(st -> findTrip(st.getTripId()))
                .map(t -> toTripResponse(t, userId, true))
                .collect(Collectors.toList());
    }

    public TripResponse createTrip(TripCreateRequest request) {
        User user = findUser(request.getUserId());
        Trip trip = new Trip();
        trip.setUser(user);
        trip.setTitle(request.getTitle());
        trip.setDestination(request.getDestination());
        trip.setStartDate(parseDate(request.getStartDate()));
        trip.setEndDate(parseDate(request.getEndDate()));
        trip.setDescription(request.getDescription());
        if (request.getCoverUrl() != null && !request.getCoverUrl().isBlank()) {
            trip.setCoverUrl(request.getCoverUrl());
        }

        if (request.getScheduleItems() != null) {
            int order = 0;
            for (ScheduleItemRequest item : request.getScheduleItems()) {
                ScheduleItem scheduleItem = new ScheduleItem();
                scheduleItem.setDayNum(item.getDayNum());
                scheduleItem.setScheduleTime(item.getScheduleTime());
                scheduleItem.setPlaceName(item.getPlaceName());
                scheduleItem.setDescription(item.getDescription());
                scheduleItem.setLatitude(resolveLat(item.getLatitude(), request.getDestination(), order));
                scheduleItem.setLongitude(resolveLng(item.getLongitude(), request.getDestination(), order));
                scheduleItem.setSortOrder(order++);
                trip.addScheduleItem(scheduleItem);
            }
        }

        tripRepository.save(trip);
        return toTripResponse(trip, user.getId(), true);
    }

    public TripResponse updateTrip(Long tripId, Long userId, TripUpdateRequest request) {
        Trip trip = findTrip(tripId);
        if (!trip.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 일정만 수정할 수 있습니다.");
        }
        findUser(userId);

        trip.setTitle(request.getTitle());
        trip.setDestination(request.getDestination());
        trip.setStartDate(parseDate(request.getStartDate()));
        trip.setEndDate(parseDate(request.getEndDate()));
        trip.setDescription(request.getDescription());
        if (request.getCoverUrl() != null) {
            trip.setCoverUrl(request.getCoverUrl().isBlank() ? null : request.getCoverUrl());
        }

        trip.getScheduleItems().clear();
        if (request.getScheduleItems() != null) {
            int order = 0;
            for (ScheduleItemRequest item : request.getScheduleItems()) {
                ScheduleItem scheduleItem = new ScheduleItem();
                scheduleItem.setDayNum(item.getDayNum());
                scheduleItem.setScheduleTime(item.getScheduleTime());
                scheduleItem.setPlaceName(item.getPlaceName());
                scheduleItem.setDescription(item.getDescription());
                scheduleItem.setLatitude(resolveLat(item.getLatitude(), request.getDestination(), order));
                scheduleItem.setLongitude(resolveLng(item.getLongitude(), request.getDestination(), order));
                scheduleItem.setSortOrder(order++);
                trip.addScheduleItem(scheduleItem);
            }
        }

        tripRepository.save(trip);
        return toTripResponse(trip, userId, true);
    }

    public List<ReviewResponse> getReviews(Long tripId) {
        findTrip(tripId);
        return reviewRepository.findByTripIdOrderByCreatedAtDesc(tripId).stream()
                .map(this::toReviewResponse)
                .collect(Collectors.toList());
    }

    public ReviewResponse addReview(Long tripId, ReviewCreateRequest request) {
        Trip trip = findTrip(tripId);
        User user = findUser(request.getUserId());
        Review review = new Review();
        review.setTrip(trip);
        review.setUser(user);
        review.setContent(request.getContent());
        review.setRating(request.getRating());
        reviewRepository.save(review);
        return toReviewResponse(review);
    }

    public void saveTrip(Long userId, Long tripId) {
        findUser(userId);
        findTrip(tripId);
        if (!savedTripRepository.existsByUserIdAndTripId(userId, tripId)) {
            savedTripRepository.save(new SavedTrip(userId, tripId));
        }
    }

    public void unsaveTrip(Long userId, Long tripId) {
        findUser(userId);
        findTrip(tripId);
        savedTripRepository.deleteByUserIdAndTripId(userId, tripId);
    }

    public void likeTrip(Long userId, Long tripId) {
        findUser(userId);
        Trip trip = findTrip(tripId);
        if (!tripLikeRepository.existsByUserIdAndTripId(userId, tripId)) {
            tripLikeRepository.save(new TripLike(userId, tripId));
            trip.setLikeCount(trip.getLikeCount() + 1);
            tripRepository.save(trip);
        }
    }

    public void unlikeTrip(Long userId, Long tripId) {
        findUser(userId);
        Trip trip = findTrip(tripId);
        if (tripLikeRepository.existsByUserIdAndTripId(userId, tripId)) {
            tripLikeRepository.deleteByUserIdAndTripId(userId, tripId);
            trip.setLikeCount(Math.max(0, trip.getLikeCount() - 1));
            tripRepository.save(trip);
        }
    }

    public List<UserSummaryResponse> getFollowers(Long userId) {
        findUser(userId);
        return followRepository.findByFollowingIdOrderByCreatedAtDesc(userId).stream()
                .map(f -> toUserSummary(findUser(f.getFollowerId())))
                .collect(Collectors.toList());
    }

    public List<UserSummaryResponse> getFollowing(Long userId) {
        findUser(userId);
        return followRepository.findByFollowerIdOrderByCreatedAtDesc(userId).stream()
                .map(f -> toUserSummary(findUser(f.getFollowingId())))
                .collect(Collectors.toList());
    }

    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = findUser(userId);
        if (request.getNickname() != null && !request.getNickname().isBlank()) {
            user.setNickname(request.getNickname().trim());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio().trim());
        }
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage().isBlank() ? null : request.getProfileImage());
        }
        userRepository.save(user);
        return toUserResponse(user);
    }

    public void follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신은 팔로우할 수 없습니다.");
        }
        findUser(followerId);
        findUser(followingId);
        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            followRepository.save(new Follow(followerId, followingId));
        }
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 없음"));
    }

    private Trip findTrip(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정 없음"));
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getBio(),
                user.getProfileImage(),
                followRepository.countByFollowingId(user.getId()),
                followRepository.countByFollowerId(user.getId()));
    }

    private UserSummaryResponse toUserSummary(User user) {
        return new UserSummaryResponse(user.getId(), user.getNickname(), user.getProfileImage());
    }

    private TripResponse toTripResponse(Trip trip, Long currentUserId, boolean includeSchedule) {
        boolean mine = currentUserId != null && trip.getUser().getId().equals(currentUserId);
        boolean saved = currentUserId != null
                && savedTripRepository.existsByUserIdAndTripId(currentUserId, trip.getId());
        boolean liked = currentUserId != null
                && tripLikeRepository.existsByUserIdAndTripId(currentUserId, trip.getId());
        boolean followingAuthor = currentUserId != null
                && !trip.getUser().getId().equals(currentUserId)
                && followRepository.existsByFollowerIdAndFollowingId(currentUserId, trip.getUser().getId());
        int reviewCount = reviewRepository.countByTripId(trip.getId());

        List<ScheduleItemResponse> items = new ArrayList<>();
        if (includeSchedule) {
            items = trip.getScheduleItems().stream()
                    .map(s -> new ScheduleItemResponse(
                            s.getDayNum(), s.getScheduleTime(), s.getPlaceName(), s.getDescription(),
                            s.getLatitude().doubleValue(), s.getLongitude().doubleValue()))
                    .collect(Collectors.toList());
        }

        return new TripResponse(
                trip.getId(),
                trip.getTitle(),
                trip.getUser().getNickname(),
                trip.getUser().getId(),
                trip.getUser().getProfileImage(),
                trip.getDestination(),
                trip.getStartDate().format(DATE_FMT),
                trip.getEndDate().format(DATE_FMT),
                trip.getDescription(),
                trip.getCoverUrl(),
                trip.getLikeCount(),
                reviewCount,
                mine,
                saved,
                liked,
                followingAuthor,
                items
        );
    }

    private ReviewResponse toReviewResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getNickname(),
                review.getContent(),
                review.getRating(),
                review.getCreatedAt().format(REVIEW_DATE_FMT)
        );
    }

    private java.time.LocalDate parseDate(String value) {
        return java.time.LocalDate.parse(value.replace('.', '-'));
    }

    private BigDecimal resolveLat(BigDecimal lat, String destination, int index) {
        if (lat != null && lat.doubleValue() != 0) return lat;
        return BigDecimal.valueOf(baseCoords(destination)[0] + index * 0.02);
    }

    private BigDecimal resolveLng(BigDecimal lng, String destination, int index) {
        if (lng != null && lng.doubleValue() != 0) return lng;
        return BigDecimal.valueOf(baseCoords(destination)[1] + index * 0.02);
    }

    private double[] baseCoords(String destination) {
        if (destination.contains("제주")) return new double[]{33.45, 126.55};
        if (destination.contains("부산")) return new double[]{35.15, 129.05};
        if (destination.contains("강릉")) return new double[]{37.75, 128.90};
        if (destination.contains("서울")) return new double[]{37.57, 126.98};
        return new double[]{37.57, 126.98};
    }
}
