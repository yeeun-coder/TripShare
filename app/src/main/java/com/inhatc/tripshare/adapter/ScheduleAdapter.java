package com.inhatc.tripshare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.tripshare.R;
import com.inhatc.tripshare.model.ScheduleItem;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_DAY_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final List<Object> items = new ArrayList<>();

    public void setSchedule(List<ScheduleItem> schedule) {
        items.clear();
        int currentDay = -1;
        for (ScheduleItem item : schedule) {
            if (item.getDay() != currentDay) {
                currentDay = item.getDay();
                items.add(currentDay);
            }
            items.add(item);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof Integer ? TYPE_DAY_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_DAY_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_day_header, parent, false);
            return new DayHeaderViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DayHeaderViewHolder) {
            int day = (int) items.get(position);
            ((DayHeaderViewHolder) holder).bind(day);
        } else {
            ScheduleItem item = (ScheduleItem) items.get(position);
            ((ScheduleViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class DayHeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDay;

        DayHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day);
        }

        void bind(int day) {
            tvDay.setText(itemView.getContext().getString(R.string.day_format, day));
        }
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final TextView tvTitle;
        private final TextView tvDescription;

        ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }

        void bind(ScheduleItem item) {
            tvTime.setText(item.getTime());
            tvTitle.setText(item.getTitle());
            tvDescription.setText(item.getDescription());
        }
    }
}
