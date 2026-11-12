package com.inhatc.tripshare;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.inhatc.tripshare.api.ApiCallback;
import com.inhatc.tripshare.api.SessionManager;
import com.inhatc.tripshare.api.TripRepository;
import com.inhatc.tripshare.api.dto.UserDto;
import com.inhatc.tripshare.api.dto.UserUpdateRequest;
import com.inhatc.tripshare.databinding.ActivityEditProfileBinding;
import com.inhatc.tripshare.util.ProfileImageHelper;

public class EditProfileActivity extends BaseActivity {

    private ActivityEditProfileBinding binding;
    private String profileImageBase64;
    private final TripRepository repository = TripRepository.getInstance();

    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    applyPickedImage(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnChangePhoto.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
        binding.btnSave.setOnClickListener(v -> saveProfile());

        profileImageBase64 = SessionManager.getProfileImage();
        ProfileImageHelper.loadInto(binding.imgProfile, profileImageBase64);

        var user = SessionManager.getProfile();
        binding.etNickname.setText(user.getNickname());
        binding.etBio.setText(user.getBio());
    }

    private void applyPickedImage(Uri uri) {
        try {
            profileImageBase64 = ProfileImageHelper.encodeUri(this, uri);
            ProfileImageHelper.loadInto(binding.imgProfile, profileImageBase64);
        } catch (Exception e) {
            Toast.makeText(this, R.string.profile_image_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfile() {
        String nickname = binding.etNickname.getText() != null
                ? binding.etNickname.getText().toString().trim() : "";
        String bio = binding.etBio.getText() != null
                ? binding.etBio.getText().toString().trim() : "";

        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnSave.setEnabled(false);
        UserUpdateRequest request = new UserUpdateRequest(nickname, bio, profileImageBase64);
        repository.updateUser(SessionManager.getUserId(), request, new ApiCallback<UserDto>() {
            @Override
            public void onSuccess(UserDto data) {
                binding.btnSave.setEnabled(true);
                SessionManager.updateCurrentUser(data.nickname, data.bio, data.profileImage);
                Toast.makeText(EditProfileActivity.this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String message) {
                binding.btnSave.setEnabled(true);
                Toast.makeText(EditProfileActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
