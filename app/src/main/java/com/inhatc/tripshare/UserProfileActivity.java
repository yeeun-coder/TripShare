package com.inhatc.tripshare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.inhatc.tripshare.adapter.TripAdapter;
import com.inhatc.tripshare.api.ApiCallback;
import com.inhatc.tripshare.api.TripRepository;
import com.inhatc.tripshare.api.dto.UserDto;
import com.inhatc.tripshare.databinding.ActivityUserProfileBinding;
import com.inhatc.tripshare.model.Trip;
import com.inhatc.tripshare.util.ProfileImageHelper;

import java.util.List;

public class UserProfileActivity extends BaseActivity implements TripAdapter.OnTripClickListener {

    public static final String EXTRA_USER_ID = "user_id";

    private ActivityUserProfileBinding binding;
    private TripAdapter adapter;
    private final TripRepository repository = TripRepository.getInstance();

    public static Intent createIntent(Context context, long userId) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        long userId = getIntent().getLongExtra(EXTRA_USER_ID, 0);
        binding.btnBack.setOnClickListener(v -> finish());

        adapter = new TripAdapter(this);
        binding.rvTrips.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTrips.setAdapter(adapter);

        repository.getUser(userId, new ApiCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto data) {
                binding.tvNickname.setText(data.nickname);
                binding.tvBio.setText(data.bio != null ? data.bio : "");
                ProfileImageHelper.loadInto(binding.imgProfile, data.profileImage);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(UserProfileActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });

        loadUserTrips(userId);
    }

    private void loadUserTrips(long userId) {
        repository.getCreatedTrips(userId, new ApiCallback<List<Trip>>() {
            @Override
            public void onSuccess(List<Trip> data) {
                adapter.setTrips(data);
                boolean empty = data.isEmpty();
                binding.rvTrips.setVisibility(empty ? View.GONE : View.VISIBLE);
                binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(UserProfileActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTripClick(Trip trip) {
        Intent intent = new Intent(this, ItineraryDetailActivity.class);
        intent.putExtra(ItineraryDetailActivity.EXTRA_TRIP_ID, trip.getId());
        startActivity(intent);
    }
}
