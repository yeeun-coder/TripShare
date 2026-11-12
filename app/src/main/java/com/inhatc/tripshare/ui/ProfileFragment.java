package com.inhatc.tripshare.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.inhatc.tripshare.EditProfileActivity;
import com.inhatc.tripshare.FollowListActivity;
import com.inhatc.tripshare.R;
import com.inhatc.tripshare.WelcomeActivity;
import com.inhatc.tripshare.adapter.MenuAdapter;
import com.inhatc.tripshare.api.ApiCallback;
import com.inhatc.tripshare.api.SessionManager;
import com.inhatc.tripshare.api.TripRepository;
import com.inhatc.tripshare.databinding.FragmentProfileBinding;
import com.inhatc.tripshare.model.UserProfile;
import com.inhatc.tripshare.util.ProfileImageHelper;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private final TripRepository repository = TripRepository.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindProfile();
        setupMenu();
        binding.btnSettings.setOnClickListener(v ->
                Toast.makeText(requireContext(), "설정 - 준비 중", Toast.LENGTH_SHORT).show());
        binding.layoutFollowers.setOnClickListener(v ->
                startActivity(FollowListActivity.createIntent(requireContext(),
                        SessionManager.getUserId(), FollowListActivity.TYPE_FOLLOWERS)));
        binding.layoutFollowing.setOnClickListener(v ->
                startActivity(FollowListActivity.createIntent(requireContext(),
                        SessionManager.getUserId(), FollowListActivity.TYPE_FOLLOWING)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding != null) {
            bindProfile();
        }
    }

    private void bindProfile() {
        UserProfile user = SessionManager.getProfile();
        binding.tvNickname.setText(user.getNickname());
        binding.tvBio.setText(user.getBio());
        ProfileImageHelper.loadInto(binding.imgProfile, SessionManager.getProfileImage());

        repository.loadProfileStats(new ApiCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile data) {
                if (binding == null) {
                    return;
                }
                binding.tvCreatedCount.setText(String.valueOf(data.getCreatedCount()));
                binding.tvSavedCount.setText(String.valueOf(data.getSavedCount()));
                binding.tvLikesCount.setText(String.valueOf(data.getLikesReceived()));
                binding.tvFollowerCount.setText(String.valueOf(data.getFollowerCount()));
                binding.tvFollowingCount.setText(String.valueOf(data.getFollowingCount()));
            }

            @Override
            public void onError(String message) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupMenu() {
        String[] menuItems = {
                getString(R.string.edit_profile),
                getString(R.string.change_password),
                getString(R.string.notification_settings),
                getString(R.string.customer_center),
                getString(R.string.logout)
        };

        MenuAdapter menuAdapter = new MenuAdapter(menuItems, menu -> {
            if (getString(R.string.logout).equals(menu)) {
                SessionManager.clear();
                Intent intent = new Intent(requireContext(), WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (getString(R.string.edit_profile).equals(menu)) {
                startActivity(new Intent(requireContext(), EditProfileActivity.class));
            } else {
                Toast.makeText(requireContext(), menu + " - 준비 중", Toast.LENGTH_SHORT).show();
            }
        });

        binding.rvMenu.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMenu.setAdapter(menuAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
