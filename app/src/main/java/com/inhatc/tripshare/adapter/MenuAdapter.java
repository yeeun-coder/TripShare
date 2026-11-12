package com.inhatc.tripshare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.tripshare.R;

import java.util.Arrays;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    public interface OnMenuClickListener {
        void onMenuClick(String menuItem);
    }

    private final List<String> menuItems;
    private final OnMenuClickListener listener;

    public MenuAdapter(String[] items, OnMenuClickListener listener) {
        this.menuItems = Arrays.asList(items);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.bind(menuItems.get(position));
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMenu;

        MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenu = itemView.findViewById(R.id.tv_menu);
        }

        void bind(String menu) {
            tvMenu.setText(menu);
            itemView.setOnClickListener(v -> listener.onMenuClick(menu));
        }
    }
}
