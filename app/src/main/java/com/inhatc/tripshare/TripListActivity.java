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
import com.inhatc.tripshare.databinding.ActivityTripListBinding;
import com.inhatc.tripshare.model.Trip;

import java.util.List;

public class TripListActivity extends BaseActivity implements TripAdapter.OnTripClickListener {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_SORT_LIKES = "sort_likes";

    private ActivityTripListBinding binding;
    private TripAdapter adapter;
    private final TripRepository repository = TripRepository.getInstance();

    public static Intent createIntent(Context context, String title, boolean sortByLikes) {
        Intent intent = new Intent(context, TripListActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_SORT_LIKES, sortByLikes);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTripListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        boolean sortByLikes = getIntent().getBooleanExtra(EXTRA_SORT_LIKES, false);

        binding.tvTitle.setText(title != null ? title : getString(R.string.popular_trips));
        binding.btnBack.setOnClickListener(v -> finish());

        adapter = new TripAdapter(this);
        binding.rvTrips.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTrips.setAdapter(adapter);

        ApiCallback<List<Trip>> callback = new ApiCallback<List<Trip>>() {
            @Override
            public void onSuccess(List<Trip> data) {
                adapter.setTrips(data);
                boolean empty = data.isEmpty();
                binding.rvTrips.setVisibility(empty ? View.GONE : View.VISIBLE);
                binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(TripListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        };

        if (sortByLikes) {
            repository.getTripsSortedByLikes(callback);
        } else {
            repository.getLatestTrips(callback);
        }
    }

    @Override
    public void onTripClick(Trip trip) {
        Intent intent = new Intent(this, ItineraryDetailActivity.class);
        intent.putExtra(ItineraryDetailActivity.EXTRA_TRIP_ID, trip.getId());
        startActivity(intent);
    }
}
