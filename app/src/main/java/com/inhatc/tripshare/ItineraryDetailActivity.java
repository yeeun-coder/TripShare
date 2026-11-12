package com.inhatc.tripshare;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.inhatc.tripshare.adapter.ReviewAdapter;
import com.inhatc.tripshare.adapter.ScheduleAdapter;
import com.inhatc.tripshare.api.ApiCallback;
import com.inhatc.tripshare.api.SessionManager;
import com.inhatc.tripshare.api.TripRepository;
import com.inhatc.tripshare.databinding.ActivityItineraryDetailBinding;
import com.inhatc.tripshare.model.Review;
import com.inhatc.tripshare.model.Trip;
import com.inhatc.tripshare.util.ProfileImageHelper;

import java.util.List;

public class ItineraryDetailActivity extends BaseActivity {

    public static final String EXTRA_TRIP_ID = "trip_id";

    private ActivityItineraryDetailBinding binding;
    private Trip trip;
    private ScheduleAdapter scheduleAdapter;
    private ReviewAdapter reviewAdapter;
    private int tripId;
    private boolean isFollowing;
    private boolean isSaved;
    private boolean isLiked;
    private int likeCount;
    private final TripRepository repository = TripRepository.getInstance();

    private final ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadTrip();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItineraryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tripId = getIntent().getIntExtra(EXTRA_TRIP_ID, 1);

        scheduleAdapter = new ScheduleAdapter();
        binding.rvSchedule.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSchedule.setAdapter(scheduleAdapter);

        reviewAdapter = new ReviewAdapter();
        binding.rvReviews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReviews.setAdapter(reviewAdapter);

        setupTabs();
        setupActions();
        loadTrip();
    }

    private void loadTrip() {
        repository.getTrip(tripId, new ApiCallback<Trip>() {
            @Override
            public void onSuccess(Trip data) {
                trip = data;
                bindTrip();
                loadReviews();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void bindTrip() {
        ProfileImageHelper.loadCover(binding.imgCover, trip.getCoverUrl(), trip.getImageRes());
        binding.tvTitle.setText(trip.getTitle());
        binding.tvAuthor.setText(trip.getAuthor());
        ProfileImageHelper.loadInto(binding.imgAuthor, trip.getAuthorProfileImage());
        binding.tvLocation.setText(trip.getLocation());
        binding.tvDates.setText(trip.getDateRange());
        binding.tvDescription.setText(trip.getDescription());
        updateReviewCount();
        updateReviewTabTitle(trip.getComments());

        scheduleAdapter.setSchedule(trip.getSchedule());

        binding.tvInfo.setText("여행지: " + trip.getLocation() + "\n"
                + "기간: " + trip.getDateRange() + "\n\n"
                + trip.getDescription());

        likeCount = trip.getLikes();
        binding.tvLikes.setText(String.valueOf(likeCount));

        isFollowing = trip.isFollowingAuthor();
        isSaved = trip.isSaved();
        isLiked = trip.isLiked();
        updateFollowButtonVisibility();
        updateFollowButtonStyle();
        updateSaveButtonStyle();
        updateLikeButtonStyle();
        updateMoreButtonVisibility();
    }

    private void setupActions() {
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnShare.setOnClickListener(v ->
                Toast.makeText(this, "공유 기능 준비 중", Toast.LENGTH_SHORT).show());
        binding.btnMore.setOnClickListener(this::showMoreMenu);
        binding.btnFollow.setOnClickListener(v -> toggleFollow());
        binding.btnSave.setOnClickListener(v -> toggleSave());
        binding.btnLike.setOnClickListener(v -> toggleLike());
        binding.layoutAuthor.setOnClickListener(v -> openAuthorProfile());
        binding.btnMap.setOnClickListener(v -> {
            if (trip == null) {
                Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra(MapActivity.EXTRA_TRIP_ID, trip.getId());
            startActivity(intent);
        });
        binding.btnSubmitReview.setOnClickListener(v -> submitReview());
    }

    private void showMoreMenu(View anchor) {
        if (trip == null || !isOwnTrip()) {
            return;
        }
        PopupMenu menu = new PopupMenu(this, anchor);
        menu.getMenu().add(0, 0, 0, R.string.edit_itinerary);
        menu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 0) {
                editLauncher.launch(CreateItineraryActivity.createEditIntent(this, trip.getId()));
                return true;
            }
            return false;
        });
        menu.show();
    }

    private boolean isOwnTrip() {
        return trip.isMine() || trip.getAuthorUserId() == SessionManager.getUserId();
    }

    private void toggleFollow() {
        if (trip == null || trip.getAuthorUserId() == 0) {
            return;
        }
        if (trip.isMine() || trip.getAuthorUserId() == SessionManager.getUserId()) {
            Toast.makeText(this, R.string.cannot_follow_self, Toast.LENGTH_SHORT).show();
            return;
        }
        if (isFollowing) {
            isFollowing = false;
            updateFollowButtonStyle();
            return;
        }

        repository.followUser(trip.getAuthorUserId(), new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                isFollowing = true;
                updateFollowButtonStyle();
                Toast.makeText(ItineraryDetailActivity.this, R.string.follow_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleLike() {
        if (trip == null) {
            return;
        }
        if (isLiked) {
            repository.unlikeTrip(trip.getId(), new ApiCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    isLiked = false;
                    likeCount = Math.max(0, likeCount - 1);
                    binding.tvLikes.setText(String.valueOf(likeCount));
                    updateLikeButtonStyle();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        repository.likeTrip(trip.getId(), new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                isLiked = true;
                likeCount += 1;
                binding.tvLikes.setText(String.valueOf(likeCount));
                updateLikeButtonStyle();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleSave() {
        if (trip == null) {
            return;
        }
        if (isSaved) {
            repository.unsaveTrip(trip.getId(), new ApiCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    isSaved = false;
                    updateSaveButtonStyle();
                    Toast.makeText(ItineraryDetailActivity.this,
                            R.string.unsaved_itinerary, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        repository.saveTrip(trip.getId(), new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                isSaved = true;
                updateSaveButtonStyle();
                Toast.makeText(ItineraryDetailActivity.this, R.string.saved_itinerary, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openAuthorProfile() {
        if (trip == null || trip.getAuthorUserId() == 0) {
            return;
        }
        if (trip.isMine() || trip.getAuthorUserId() == SessionManager.getUserId()) {
            return;
        }
        startActivity(UserProfileActivity.createIntent(this, trip.getAuthorUserId()));
    }

    private void updateFollowButtonVisibility() {
        binding.btnFollow.setVisibility(isOwnTrip() ? View.GONE : View.VISIBLE);
    }

    private void updateMoreButtonVisibility() {
        binding.btnMore.setVisibility(isOwnTrip() ? View.VISIBLE : View.GONE);
    }

    private void updateSaveButtonStyle() {
        int color = isSaved
                ? ContextCompat.getColor(this, R.color.purple_primary)
                : ContextCompat.getColor(this, R.color.text_hint);
        binding.btnSave.setImageTintList(ColorStateList.valueOf(color));
    }

    private void updateLikeButtonStyle() {
        int color = isLiked
                ? ContextCompat.getColor(this, R.color.purple_primary)
                : ContextCompat.getColor(this, R.color.text_hint);
        binding.btnLike.setImageTintList(ColorStateList.valueOf(color));
    }

    private void updateFollowButtonStyle() {
        if (isFollowing) {
            binding.btnFollow.setText(R.string.following);
            binding.btnFollow.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.transparent)));
            binding.btnFollow.setStrokeColor(
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.purple_primary)));
            binding.btnFollow.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.stroke_width));
            binding.btnFollow.setTextColor(ContextCompat.getColor(this, R.color.purple_primary));
        } else {
            binding.btnFollow.setText(R.string.follow);
            binding.btnFollow.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.purple_primary)));
            binding.btnFollow.setStrokeWidth(0);
            binding.btnFollow.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    private void setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                binding.rvSchedule.setVisibility(pos == 0 ? View.VISIBLE : View.GONE);
                binding.tvInfo.setVisibility(pos == 1 ? View.VISIBLE : View.GONE);
                binding.layoutReviews.setVisibility(pos == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void submitReview() {
        String content = binding.etReview.getText() != null
                ? binding.etReview.getText().toString().trim() : "";
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, R.string.review_hint, Toast.LENGTH_SHORT).show();
            return;
        }

        int rating = Math.round(binding.ratingInput.getRating());
        if (rating < 1) {
            rating = 1;
        }

        repository.addReview(tripId, content, rating, new ApiCallback<Review>() {
            @Override
            public void onSuccess(Review data) {
                binding.etReview.getText().clear();
                binding.ratingInput.setRating(5);
                loadReviews();
                Toast.makeText(ItineraryDetailActivity.this, R.string.review_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadReviews() {
        repository.getReviews(tripId, new ApiCallback<List<Review>>() {
            @Override
            public void onSuccess(List<Review> data) {
                reviewAdapter.setReviews(data);
                binding.tvReviewEmpty.setVisibility(
                        reviewAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                binding.tvComments.setText(String.valueOf(data.size()));
                updateReviewTabTitle(data.size());
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ItineraryDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateReviewCount() {
        if (trip != null) {
            binding.tvComments.setText(String.valueOf(trip.getComments()));
        }
    }

    private void updateReviewTabTitle(int count) {
        TabLayout.Tab reviewTab = binding.tabLayout.getTabAt(2);
        if (reviewTab != null) {
            reviewTab.setText(getString(R.string.tab_reviews, count));
        }
    }
}
