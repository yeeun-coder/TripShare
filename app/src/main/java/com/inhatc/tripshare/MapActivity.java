package com.inhatc.tripshare;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inhatc.tripshare.api.ApiCallback;
import com.inhatc.tripshare.api.TripRepository;
import com.inhatc.tripshare.databinding.ActivityMapBinding;
import com.inhatc.tripshare.model.ScheduleItem;
import com.inhatc.tripshare.model.Trip;
import com.inhatc.tripshare.util.MarkerUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapActivity";

    public static final String EXTRA_TRIP_ID = "trip_id";

    private ActivityMapBinding binding;
    private GoogleMap googleMap;
    private Trip trip;
    private boolean mapRendered;
    private final Map<Marker, ScheduleItem> markerData = new HashMap<>();
    private final TripRepository repository = TripRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int tripId = getIntent().getIntExtra(EXTRA_TRIP_ID, 1);
        binding.btnBack.setOnClickListener(v -> finish());

        try {
            MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST,
                    renderer -> Log.d(TAG, "Maps renderer: " + renderer));
        } catch (Exception e) {
            Log.e(TAG, "Maps SDK init failed", e);
            Toast.makeText(this, R.string.map_load_error, Toast.LENGTH_LONG).show();
        }

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_container, mapFragment)
                .commitNow();
        mapFragment.getMapAsync(this);

        repository.getTrip(tripId, new ApiCallback<Trip>() {
            @Override
            public void onSuccess(Trip data) {
                trip = data;
                renderMapIfReady();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MapActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnMarkerClickListener(this);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        renderMapIfReady();
    }

    private void renderMapIfReady() {
        if (googleMap == null || trip == null || mapRendered) {
            return;
        }

        mapRendered = true;
        markerData.clear();
        googleMap.clear();

        List<ScheduleItem> schedule = trip.getSchedule();
        if (schedule.isEmpty()) {
            Toast.makeText(this, R.string.map_no_schedule, Toast.LENGTH_SHORT).show();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(37.5665, 126.9780), 7f));
            return;
        }

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        for (int i = 0; i < schedule.size(); i++) {
            ScheduleItem item = schedule.get(i);
            LatLng position = new LatLng(item.getLatitude(), item.getLongitude());
            boundsBuilder.include(position);

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(item.getTitle())
                    .icon(MarkerUtils.createNumberedMarker(this, i + 1)));
            if (marker != null) {
                markerData.put(marker, item);
            }
        }

        try {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                    boundsBuilder.build(), 120));
        } catch (IllegalStateException e) {
            LatLng first = new LatLng(schedule.get(0).getLatitude(), schedule.get(0).getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(first, 10f));
        }

        showSpotInfo(schedule.get(0));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        ScheduleItem item = markerData.get(marker);
        if (item != null) {
            showSpotInfo(item);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 12f));
        }
        return true;
    }

    private void showSpotInfo(ScheduleItem item) {
        binding.cardSpotInfo.setVisibility(View.VISIBLE);
        binding.tvSpotTitle.setText(
                getString(R.string.day_format, item.getDay()) + " - " + item.getTitle());
        binding.tvSpotTime.setText(item.getTime());
        binding.tvSpotDesc.setText(item.getDescription());
        binding.imgSpot.setBackgroundResource(trip.getImageRes());
    }
}
