package com.inhatc.tripshare.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.inhatc.tripshare.ItineraryDetailActivity;
import com.inhatc.tripshare.R;
import com.inhatc.tripshare.TripListActivity;
import com.inhatc.tripshare.adapter.TripAdapter;
import com.inhatc.tripshare.api.ApiCallback;
import com.inhatc.tripshare.api.TripRepository;
import com.inhatc.tripshare.databinding.FragmentHomeBinding;
import com.inhatc.tripshare.model.Trip;

import java.util.List;

public class HomeFragment extends Fragment implements TripAdapter.OnTripClickListener {

    private FragmentHomeBinding binding;
    private TripAdapter popularAdapter;
    private TripAdapter latestAdapter;
    private TripAdapter searchAdapter;
    private final TripRepository repository = TripRepository.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        popularAdapter = new TripAdapter(this);
        latestAdapter = new TripAdapter(this);
        searchAdapter = new TripAdapter(this);

        binding.rvPopular.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPopular.setAdapter(popularAdapter);

        binding.rvLatest.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvLatest.setAdapter(latestAdapter);

        binding.rvSearch.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSearch.setAdapter(searchAdapter);

        loadTrips();

        binding.btnNotifications.setOnClickListener(v ->
                Toast.makeText(requireContext(), R.string.notifications_coming, Toast.LENGTH_SHORT).show());

        binding.btnPopularMore.setOnClickListener(v ->
                startActivity(TripListActivity.createIntent(
                        requireContext(),
                        getString(R.string.popular_trips),
                        true)));

        binding.etSearch.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(binding.etSearch.getText() != null
                        ? binding.etSearch.getText().toString() : "");
                return true;
            }
            return false;
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s != null ? s.toString() : "");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadTrips() {
        repository.getPopularTrips(tripCallback(popularAdapter));
        repository.getLatestTrips(tripCallback(latestAdapter));
    }

    private void performSearch(String query) {
        if (TextUtils.isEmpty(query.trim())) {
            showHomeSections();
            loadTrips();
            return;
        }

        showSearchSection();
        repository.searchTrips(query, new ApiCallback<List<Trip>>() {
            @Override
            public void onSuccess(List<Trip> data) {
                if (!isAdded()) {
                    return;
                }
                searchAdapter.setTrips(data);
                boolean empty = data.isEmpty();
                binding.rvSearch.setVisibility(empty ? View.GONE : View.VISIBLE);
                binding.tvSearchEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(String message) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showSearchSection() {
        binding.sectionHome.setVisibility(View.GONE);
        binding.sectionSearch.setVisibility(View.VISIBLE);
    }

    private void showHomeSections() {
        binding.sectionSearch.setVisibility(View.GONE);
        binding.sectionHome.setVisibility(View.VISIBLE);
        binding.tvSearchEmpty.setVisibility(View.GONE);
        binding.rvSearch.setVisibility(View.VISIBLE);
    }

    private ApiCallback<List<Trip>> tripCallback(TripAdapter adapter) {
        return new ApiCallback<List<Trip>>() {
            @Override
            public void onSuccess(List<Trip> data) {
                adapter.setTrips(data);
            }

            @Override
            public void onError(String message) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onTripClick(Trip trip) {
        Intent intent = new Intent(requireContext(), ItineraryDetailActivity.class);
        intent.putExtra(ItineraryDetailActivity.EXTRA_TRIP_ID, trip.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
