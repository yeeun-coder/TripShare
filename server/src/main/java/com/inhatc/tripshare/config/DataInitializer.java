package com.inhatc.tripshare.config;

import com.inhatc.tripshare.entity.*;
import com.inhatc.tripshare.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final ReviewRepository reviewRepository;
    private final SavedTripRepository savedTripRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           TripRepository tripRepository,
                           ReviewRepository reviewRepository,
                           SavedTripRepository savedTripRepository,
                           FollowRepository followRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.reviewRepository = reviewRepository;
        this.savedTripRepository = savedTripRepository;
        this.followRepository = followRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        // === 테스트 계정 (비밀번호 모두 1234) ===
        User yuni = createUser("yuni@example.com", "1234", "traveler_유니",
                "제주·강릉 힐링 여행을 좋아해요 ✈️");
        User minsu = createUser("foodie@example.com", "1234", "foodie_민수",
                "맛집 탐방이 취미입니다 🍜");
        User jieun = createUser("culture@example.com", "1234", "culture_지은",
                "박물관·전통 문화 여행러 📚");
        User haneul = createUser("ocean@example.com", "1234", "ocean_하늘",
                "바다와 카페 투어를 즐깁니다 🌊");
        User soo = createUser("camper@example.com", "1234", "camper_수",
                "캠핑·자연 여행 전문 🏕️");
        User dahee = createUser("photo@example.com", "1234", "photo_다희",
                "사진 찍으며 여행합니다 📷");

        // === 유니 일정 ===
        Trip jeju = createTrip(yuni, "제주도 2박 3일 힐링 여행", "제주도",
                "2024-05-01", "2024-05-03",
                "제주의 아름다운 자연을 만끽하는 힐링 여행 일정입니다.", 245);
        addSchedule(jeju, 1, "09:00", "제주공항 도착", "렌터카 픽업", 33.5113, 126.4930, 0);
        addSchedule(jeju, 1, "12:00", "협재해수욕장", "에메랄드빛 바다", 33.3939, 126.2396, 1);
        addSchedule(jeju, 2, "09:00", "성산일출봉", "일출봉 등반", 33.4581, 126.9425, 2);
        addSchedule(jeju, 2, "14:00", "섭지코지", "드라마 촬영지", 33.4244, 126.9278, 3);
        addSchedule(jeju, 3, "10:00", "한라산 어리목", "가벼운 등산", 33.3617, 126.5292, 4);
        tripRepository.save(jeju);

        Trip jejuFood = createTrip(yuni, "제주 맛집 & 카페 투어", "제주도",
                "2024-08-15", "2024-08-16",
                "현지인 추천 맛집과 감성 카페 코스.", 128);
        addSchedule(jejuFood, 1, "11:00", "돈사돈", "흑돼지 구이", 33.4996, 126.5312, 0);
        addSchedule(jejuFood, 1, "15:00", "애월 카페거리", "오션뷰 카페", 33.4626, 126.3089, 1);
        tripRepository.save(jejuFood);

        // === 민수 일정 ===
        Trip busan = createTrip(minsu, "부산 맛집 투어 1박 2일", "부산",
                "2024-06-10", "2024-06-11",
                "부산의 유명 맛집을 돌아보는 미식 여행입니다.", 189);
        addSchedule(busan, 1, "11:00", "자갈치시장", "해산물 점심", 35.0967, 129.0307, 0);
        addSchedule(busan, 1, "15:00", "해운대 해수욕장", "해변 산책", 35.1587, 129.1604, 1);
        tripRepository.save(busan);

        Trip busanNight = createTrip(minsu, "부산 야경 & 포장마차", "부산",
                "2024-09-20", "2024-09-21",
                "광안대교 야경과 서면 포장마차.", 97);
        addSchedule(busanNight, 1, "18:00", "광안리 해수욕장", "야경 감상", 35.1532, 129.1186, 0);
        addSchedule(busanNight, 1, "20:00", "서면 포장마차", "길거리 음식", 35.1579, 129.0590, 1);
        tripRepository.save(busanNight);

        // === 지은 일정 ===
        Trip seoul = createTrip(jieun, "서울 문화 탐방 1일 코스", "서울",
                "2024-04-20", "2024-04-20",
                "서울의 대표 문화 명소를 하루에 둘러보는 코스입니다.", 156);
        addSchedule(seoul, 1, "10:00", "경복궁", "한복 체험", 37.5796, 126.9770, 0);
        addSchedule(seoul, 1, "14:00", "북촌한옥마을", "전통 마을 산책", 37.5826, 126.9831, 1);
        tripRepository.save(seoul);

        Trip seoulMuseum = createTrip(jieun, "서울 미술관 투어", "서울",
                "2024-10-05", "2024-10-06",
                "국립중앙박물관·이태원·한남동 갤러리.", 88);
        addSchedule(seoulMuseum, 1, "10:00", "국립중앙박물관", "상설 전시", 37.5240, 126.9803, 0);
        addSchedule(seoulMuseum, 2, "14:00", "리움미술관", "현대미술", 37.5394, 126.9996, 1);
        tripRepository.save(seoulMuseum);

        // === 하늘 일정 ===
        Trip gangneung = createTrip(haneul, "강릉 바다 여행 2박 3일", "강릉",
                "2024-07-01", "2024-07-03",
                "동해의 푸른 바다와 커피 거리를 즐기는 여행입니다.", 312);
        addSchedule(gangneung, 1, "09:00", "경포대", "해변 산책", 37.7955, 128.9008, 0);
        addSchedule(gangneung, 2, "11:00", "안목해변 커피거리", "카페 투어", 37.7703, 128.9463, 1);
        tripRepository.save(gangneung);

        Trip sokcho = createTrip(haneul, "속초·양양 서핑 여행", "속초",
                "2024-08-01", "2024-08-02",
                "서핑과 속초 중앙시장 미식.", 201);
        addSchedule(sokcho, 1, "08:00", "죽도해변", "서핑 레슨", 38.1975, 128.6195, 0);
        addSchedule(sokcho, 1, "17:00", "속초 중앙시장", "닭강정·오징어", 38.2070, 128.5910, 1);
        tripRepository.save(sokcho);

        // === 수 일정 ===
        Trip gapyeong = createTrip(soo, "가평 캠핑 1박 2일", "가평",
                "2024-06-01", "2024-06-02",
                "자연 속 글램핑 & BBQ.", 76);
        addSchedule(gapyeong, 1, "14:00", "남이섬", "산책·보트", 37.7906, 127.5255, 0);
        addSchedule(gapyeong, 2, "09:00", "쁘띠프랑스", "마을 구경", 37.7153, 127.5089, 1);
        tripRepository.save(gapyeong);

        // === 다희 일정 ===
        Trip jeonju = createTrip(dahee, "전주 한옥마을 사진 여행", "전주",
                "2024-05-20", "2024-05-21",
                "한옥마을·경기전·비빔밥.", 142);
        addSchedule(jeonju, 1, "10:00", "전주 한옥마을", "골목 사진", 35.8154, 127.1530, 0);
        addSchedule(jeonju, 1, "13:00", "경기전", "역사 탐방", 35.8127, 127.1496, 1);
        tripRepository.save(jeonju);

        // === 후기 ===
        addReview(jeju, minsu, "성산일출봉 일출이 정말 아름다웠어요!", 5);
        addReview(jeju, jieun, "일정 구성이 알차서 따라가기 좋았습니다.", 4);
        addReview(busan, haneul, "맛집 리스트가 최고예요!", 5);
        addReview(gangneung, yuni, "커피거리 너무 좋았어요 ☕", 5);
        addReview(seoul, dahee, "경복궁 한복 체험 추천!", 4);
        addReview(jeonju, minsu, "사진 스팟이 많아요", 5);

        // === 저장 ===
        savedTripRepository.save(new SavedTrip(yuni.getId(), busan.getId()));
        savedTripRepository.save(new SavedTrip(yuni.getId(), gangneung.getId()));
        savedTripRepository.save(new SavedTrip(yuni.getId(), seoulMuseum.getId()));
        savedTripRepository.save(new SavedTrip(minsu.getId(), jeju.getId()));
        savedTripRepository.save(new SavedTrip(jieun.getId(), sokcho.getId()));
        savedTripRepository.save(new SavedTrip(haneul.getId(), jejuFood.getId()));

        // === 팔로우 ===
        followRepository.save(new Follow(yuni.getId(), minsu.getId()));
        followRepository.save(new Follow(yuni.getId(), haneul.getId()));
        followRepository.save(new Follow(minsu.getId(), yuni.getId()));
        followRepository.save(new Follow(jieun.getId(), dahee.getId()));
        followRepository.save(new Follow(dahee.getId(), yuni.getId()));
        followRepository.save(new Follow(soo.getId(), haneul.getId()));
    }

    private User createUser(String email, String password, String nickname, String bio) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);
        user.setBio(bio);
        return userRepository.save(user);
    }

    private Trip createTrip(User user, String title, String destination,
                            String start, String end, String description, int likes) {
        Trip trip = new Trip();
        trip.setUser(user);
        trip.setTitle(title);
        trip.setDestination(destination);
        trip.setStartDate(LocalDate.parse(start));
        trip.setEndDate(LocalDate.parse(end));
        trip.setDescription(description);
        trip.setLikeCount(likes);
        return trip;
    }

    private void addSchedule(Trip trip, int day, String time, String place,
                             String desc, double lat, double lng, int order) {
        ScheduleItem item = new ScheduleItem();
        item.setDayNum(day);
        item.setScheduleTime(time);
        item.setPlaceName(place);
        item.setDescription(desc);
        item.setLatitude(BigDecimal.valueOf(lat));
        item.setLongitude(BigDecimal.valueOf(lng));
        item.setSortOrder(order);
        trip.addScheduleItem(item);
    }

    private void addReview(Trip trip, User user, String content, int rating) {
        Review review = new Review();
        review.setTrip(trip);
        review.setUser(user);
        review.setContent(content);
        review.setRating(rating);
        reviewRepository.save(review);
    }
}
