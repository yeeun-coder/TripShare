package com.inhatc.tripshare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inhatc.tripshare.databinding.ActivityPlacePickerBinding;
import com.inhatc.tripshare.util.GeocoderUtil;

public class PlacePickerActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener {

    private static final String TAG = "PlacePickerActivity";

    public static final String EXTRA_DESTINATION = "destination";
    public static final String EXTRA_LAT = "lat";
    public static final String EXTRA_LNG = "lng";

    public static final String RESULT_LAT = "result_lat";
    public static final String RESULT_LNG = "result_lng";
    public static final String RESULT_PLACE = "result_place";

    private ActivityPlacePickerBinding binding;
    private GoogleMap googleMap;
    private Marker marker;
    private LatLng selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlacePickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnConfirm.setOnClickListener(v -> confirmSelection());

        try {
            MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST,
                    renderer -> Log.d(TAG, "Maps renderer: " + renderer));
        } catch (Exception e) {
            Log.e(TAG, "Maps SDK init failed", e);
        }

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_container, mapFragment)
                .commitNow();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnMapClickListener(this);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        double lat = getIntent().getDoubleExtra(EXTRA_LAT, 0);
        double lng = getIntent().getDoubleExtra(EXTRA_LNG, 0);
        LatLng initial;
        if (lat != 0 || lng != 0) {
            initial = new LatLng(lat, lng);
            setSelectedLocation(initial);
        } else {
            String destination = getIntent().getStringExtra(EXTRA_DESTINATION);
            initial = GeocoderUtil.geocode(this, destination);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initial, 11f));
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        setSelectedLocation(latLng);
    }

    private void setSelectedLocation(LatLng latLng) {
        selected = latLng;
        if (marker != null) {
            marker.remove();
        }
        marker = googleMap.addMarker(new MarkerOptions().position(latLng).title("선택한 장소"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));

        String label = GeocoderUtil.reverseGeocode(this, latLng.latitude, latLng.longitude);
        binding.tvSelected.setText(label);
        binding.btnConfirm.setEnabled(true);
    }

    private void confirmSelection() {
        if (selected == null) {
            return;
        }
        Intent data = new Intent();
        data.putExtra(RESULT_LAT, selected.latitude);
        data.putExtra(RESULT_LNG, selected.longitude);
        data.putExtra(RESULT_PLACE, binding.tvSelected.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}
