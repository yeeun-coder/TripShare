package com.inhatc.tripshare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.inhatc.tripshare.adapter.UserListAdapter;
import com.inhatc.tripshare.api.ApiCallback;
import com.inhatc.tripshare.api.SessionManager;
import com.inhatc.tripshare.api.TripRepository;
import com.inhatc.tripshare.api.dto.UserSummaryDto;
import com.inhatc.tripshare.databinding.ActivityFollowListBinding;

import java.util.List;

public class FollowListActivity extends BaseActivity implements UserListAdapter.OnUserClickListener {

    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_TYPE = "type";
    public static final String TYPE_FOLLOWERS = "followers";
    public static final String TYPE_FOLLOWING = "following";

    private ActivityFollowListBinding binding;
    private UserListAdapter adapter;
    private final TripRepository repository = TripRepository.getInstance();

    public static Intent createIntent(Context context, long userId, String type) {
        Intent intent = new Intent(context, FollowListActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        long userId = getIntent().getLongExtra(EXTRA_USER_ID, SessionManager.getUserId());
        String type = getIntent().getStringExtra(EXTRA_TYPE);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.tvTitle.setText(TYPE_FOLLOWING.equals(type)
                ? R.string.following_list_title
                : R.string.followers_title);

        adapter = new UserListAdapter(this);
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvUsers.setAdapter(adapter);

        if (TYPE_FOLLOWING.equals(type)) {
            loadFollowing(userId);
        } else {
            loadFollowers(userId);
        }
    }

    private void loadFollowers(long userId) {
        repository.getFollowers(userId, new ApiCallback<List<UserSummaryDto>>() {
            @Override
            public void onSuccess(List<UserSummaryDto> data) {
                showUsers(data);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(FollowListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFollowing(long userId) {
        repository.getFollowing(userId, new ApiCallback<List<UserSummaryDto>>() {
            @Override
            public void onSuccess(List<UserSummaryDto> data) {
                showUsers(data);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(FollowListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUsers(List<UserSummaryDto> data) {
        adapter.setUsers(data);
        boolean empty = data == null || data.isEmpty();
        binding.rvUsers.setVisibility(empty ? View.GONE : View.VISIBLE);
        binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onUserClick(UserSummaryDto user) {
        if (user.id == null || user.id == SessionManager.getUserId()) {
            return;
        }
        startActivity(UserProfileActivity.createIntent(this, user.id));
    }
}
