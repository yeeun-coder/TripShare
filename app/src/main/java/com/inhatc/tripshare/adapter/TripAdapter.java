package com.inhatc.tripshare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.tripshare.R;
import com.inhatc.tripshare.model.Trip;
import com.inhatc.tripshare.util.ProfileImageHelper;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }

    private final List<Trip> trips = new ArrayList<>();
    private final OnTripClickListener listener;

    public TripAdapter(OnTripClickListener listener) {
        this.listener = listener;
    }

    public void setTrips(List<Trip> newTrips) {
        trips.clear();
        trips.addAll(newTrips);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip_card, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        holder.bind(trips.get(position));
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    class TripViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgThumbnail;
        private final TextView tvTitle;
        private final TextView tvAuthor;
        private final TextView tvLocation;
        private final TextView tvLikes;
        private final TextView tvComments;
        private final TextView tvDates;

        TripViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.img_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvLikes = itemView.findViewById(R.id.tv_likes);
            tvComments = itemView.findViewById(R.id.tv_comments);
            tvDates = itemView.findViewById(R.id.tv_dates);
        }

        void bind(Trip trip) {
            ProfileImageHelper.loadCover(imgThumbnail, trip.getCoverUrl(), trip.getImageRes());
            tvTitle.setText(trip.getTitle());
            tvAuthor.setText(trip.getAuthor());
            tvLocation.setText(trip.getLocation());
            tvLikes.setText(String.valueOf(trip.getLikes()));
            tvComments.setText(String.valueOf(trip.getComments()));
            if (tvDates != null) {
                tvDates.setText(trip.getDateRange());
                tvDates.setVisibility(View.VISIBLE);
            }
            itemView.setOnClickListener(v -> listener.onTripClick(trip));
        }
    }
}
