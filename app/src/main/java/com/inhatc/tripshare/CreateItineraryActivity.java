package com.inhatc.tripshare;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.model.LatLng;
import com.inhatc.tripshare.adapter.ScheduleInputAdapter;
import com.inhatc.tripshare.api.ApiCallback;
import com.inhatc.tripshare.api.SessionManager;
import com.inhatc.tripshare.api.TripRepository;
import com.inhatc.tripshare.api.dto.ScheduleItemRequest;
import com.inhatc.tripshare.api.dto.TripCreateRequest;
import com.inhatc.tripshare.api.dto.TripUpdateRequest;
import com.inhatc.tripshare.databinding.ActivityCreateItineraryBinding;
import com.inhatc.tripshare.model.ScheduleInput;
import com.inhatc.tripshare.model.ScheduleItem;
import com.inhatc.tripshare.model.Trip;
import com.inhatc.tripshare.util.GeocoderUtil;
import com.inhatc.tripshare.util.ProfileImageHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateItineraryActivity extends BaseActivity {

    public static final String EXTRA_TRIP_ID = "trip_id";

    private ActivityCreateItineraryBinding binding;
    private ScheduleInputAdapter scheduleAdapter;
    private int pickingPosition = -1;
    private int editTripId;
    private boolean editMode;
    private String coverImageBase64;
    private final TripRepository repository = TripRepository.getInstance();

    public static Intent createEditIntent(Context context, int tripId) {
        Intent intent = new Intent(context, CreateItineraryActivity.class);
        intent.putExtra(EXTRA_TRIP_ID, tripId);
        return intent;
    }

    private final ActivityResultLauncher<String> pickCoverLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    applyCoverImage(uri);
                }
            });

    private final ActivityResultLauncher<Intent> placePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() != RESULT_OK || result.getData() == null) {
                    return;
                }
                Intent data = result.getData();
                double lat = data.getDoubleExtra(PlacePickerActivity.RESULT_LAT, 0);
                double lng = data.getDoubleExtra(PlacePickerActivity.RESULT_LNG, 0);
                String place = data.getStringExtra(PlacePickerActivity.RESULT_PLACE);
                if (pickingPosition < 0) {
                    return;
                }
                ScheduleInput input = scheduleAdapter.getItems().get(pickingPosition);
                input.setLocation(lat, lng);
                if (!TextUtils.isEmpty(place) && TextUtils.isEmpty(input.getPlace())) {
                    input.setPlace(place);
                }
                scheduleAdapter.updateItem(pickingPosition, input);
                pickingPosition = -1;
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateItineraryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editTripId = getIntent().getIntExtra(EXTRA_TRIP_ID, 0);
        editMode = editTripId > 0;

        scheduleAdapter = new ScheduleInputAdapter(() -> { }, this::openPlacePicker);
        binding.rvScheduleInput.setLayoutManager(new LinearLayoutManager(this));
        binding.rvScheduleInput.setAdapter(scheduleAdapter);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> saveItinerary());
        binding.btnAddSchedule.setOnClickListener(v -> scheduleAdapter.addItem());
        binding.layoutCoverPhoto.setOnClickListener(v -> pickCoverLauncher.launch("image/*"));

        binding.etStartDate.setOnClickListener(v -> showDatePicker(binding.etStartDate));
        binding.etEndDate.setOnClickListener(v -> showDatePicker(binding.etEndDate));

        if (editMode) {
            binding.tvToolbarTitle.setText(R.string.edit_itinerary);
            loadTripForEdit();
        }
    }

    private void loadTripForEdit() {
        repository.getTrip(editTripId, new ApiCallback<Trip>() {
            @Override
            public void onSuccess(Trip trip) {
                if (!trip.isMine() && trip.getAuthorUserId() != SessionManager.getUserId()) {
                    Toast.makeText(CreateItineraryActivity.this,
                            R.string.cannot_edit_others_trip, Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                populateForm(trip);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(CreateItineraryActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateForm(Trip trip) {
        binding.etTitle.setText(trip.getTitle());
        binding.etDestination.setText(trip.getLocation());
        binding.etStartDate.setText(trip.getStartDate());
        binding.etEndDate.setText(trip.getEndDate());
        binding.etIntro.setText(trip.getDescription());

        coverImageBase64 = trip.getCoverUrl();
        if (coverImageBase64 != null && !coverImageBase64.isBlank()) {
            binding.imgCover.setVisibility(View.VISIBLE);
            binding.layoutCoverPlaceholder.setVisibility(View.GONE);
            ProfileImageHelper.loadCover(binding.imgCover, coverImageBase64, R.drawable.bg_dashed_box);
        }

        List<ScheduleInput> inputs = new ArrayList<>();
        for (ScheduleItem item : trip.getSchedule()) {
            ScheduleInput input = new ScheduleInput();
            input.setDay(item.getDay());
            input.setTime(item.getTime());
            input.setPlace(item.getTitle());
            input.setDescription(item.getDescription());
            input.setLocation(item.getLatitude(), item.getLongitude());
            inputs.add(input);
        }
        scheduleAdapter.setItems(inputs);
    }

    private void applyCoverImage(Uri uri) {
        try {
            coverImageBase64 = ProfileImageHelper.encodeUri(this, uri);
            binding.imgCover.setVisibility(View.VISIBLE);
            binding.layoutCoverPlaceholder.setVisibility(View.GONE);
            ProfileImageHelper.loadCover(binding.imgCover, coverImageBase64, R.drawable.bg_dashed_box);
        } catch (Exception e) {
            Toast.makeText(this, R.string.profile_image_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void openPlacePicker(int position, ScheduleInput input) {
        pickingPosition = position;
        Intent intent = new Intent(this, PlacePickerActivity.class);
        intent.putExtra(PlacePickerActivity.EXTRA_DESTINATION, getText(binding.etDestination));
        if (input.hasLocation()) {
            intent.putExtra(PlacePickerActivity.EXTRA_LAT, input.getLatitude());
            intent.putExtra(PlacePickerActivity.EXTRA_LNG, input.getLongitude());
        }
        placePickerLauncher.launch(intent);
    }

    private void showDatePicker(com.google.android.material.textfield.TextInputEditText target) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format("%d.%02d.%02d", year, month + 1, dayOfMonth);
            target.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveItinerary() {
        binding.rvScheduleInput.clearFocus();

        String title = getText(binding.etTitle);
        String destination = getText(binding.etDestination);
        String startDate = getText(binding.etStartDate);
        String endDate = getText(binding.etEndDate);
        String intro = getText(binding.etIntro);

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(destination)
                || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        List<ScheduleItemRequest> scheduleItems = buildScheduleItems(destination);
        if (scheduleItems.isEmpty()) {
            Toast.makeText(this, R.string.schedule_required, Toast.LENGTH_SHORT).show();
            return;
        }

        String description = TextUtils.isEmpty(intro) ? title + " 여행 일정입니다." : intro;
        binding.btnSave.setEnabled(false);

        if (editMode) {
            updateItinerary(title, destination, startDate, endDate, description, scheduleItems);
        } else {
            createItinerary(title, destination, startDate, endDate, description, scheduleItems);
        }
    }

    private void createItinerary(String title, String destination, String startDate, String endDate,
                                 String description, List<ScheduleItemRequest> scheduleItems) {
        TripCreateRequest request = new TripCreateRequest(
                SessionManager.getUserId(),
                title,
                destination,
                startDate,
                endDate,
                description,
                coverImageBase64,
                scheduleItems);

        repository.createTrip(request, new ApiCallback<Trip>() {
            @Override
            public void onSuccess(Trip trip) {
                binding.btnSave.setEnabled(true);
                Toast.makeText(CreateItineraryActivity.this, R.string.itinerary_registered, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateItineraryActivity.this, ItineraryDetailActivity.class);
                intent.putExtra(ItineraryDetailActivity.EXTRA_TRIP_ID, trip.getId());
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                binding.btnSave.setEnabled(true);
                Toast.makeText(CreateItineraryActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateItinerary(String title, String destination, String startDate, String endDate,
                                 String description, List<ScheduleItemRequest> scheduleItems) {
        TripUpdateRequest request = new TripUpdateRequest(
                title,
                destination,
                startDate,
                endDate,
                description,
                coverImageBase64,
                scheduleItems);

        repository.updateTrip(editTripId, request, new ApiCallback<Trip>() {
            @Override
            public void onSuccess(Trip trip) {
                binding.btnSave.setEnabled(true);
                Toast.makeText(CreateItineraryActivity.this, R.string.itinerary_updated, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(String message) {
                binding.btnSave.setEnabled(true);
                Toast.makeText(CreateItineraryActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<ScheduleItemRequest> buildScheduleItems(String destination) {
        List<ScheduleItemRequest> result = new ArrayList<>();
        for (ScheduleInput input : scheduleAdapter.getItems()) {
            if (TextUtils.isEmpty(input.getPlace())) {
                continue;
            }
            String desc = TextUtils.isEmpty(input.getDescription())
                    ? input.getPlace() + " 방문" : input.getDescription();

            double lat = input.getLatitude();
            double lng = input.getLongitude();
            if (!input.hasLocation()) {
                String query = destination + " " + input.getPlace();
                LatLng geocoded = GeocoderUtil.geocode(this, query);
                lat = geocoded.latitude;
                lng = geocoded.longitude;
            }

            result.add(new ScheduleItemRequest(
                    input.getDay(),
                    input.getTime(),
                    input.getPlace(),
                    desc,
                    lat,
                    lng));
        }
        return result;
    }

    private String getText(com.google.android.material.textfield.TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}
