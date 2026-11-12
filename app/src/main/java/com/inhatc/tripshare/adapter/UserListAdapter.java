package com.inhatc.tripshare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.tripshare.R;
import com.inhatc.tripshare.api.dto.UserSummaryDto;
import com.inhatc.tripshare.util.ProfileImageHelper;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    public interface OnUserClickListener {
        void onUserClick(UserSummaryDto user);
    }

    private final List<UserSummaryDto> users = new ArrayList<>();
    private final OnUserClickListener listener;

    public UserListAdapter(OnUserClickListener listener) {
        this.listener = listener;
    }

    public void setUsers(List<UserSummaryDto> newUsers) {
        users.clear();
        if (newUsers != null) {
            users.addAll(newUsers);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_row, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgProfile;
        private final TextView tvNickname;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.img_profile);
            tvNickname = itemView.findViewById(R.id.tv_nickname);
        }

        void bind(UserSummaryDto user) {
            tvNickname.setText(user.nickname != null ? user.nickname : "");
            ProfileImageHelper.loadInto(imgProfile, user.profileImage);
            itemView.setOnClickListener(v -> listener.onUserClick(user));
        }
    }
}
