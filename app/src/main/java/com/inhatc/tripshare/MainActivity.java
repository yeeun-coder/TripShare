package com.inhatc.tripshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.inhatc.tripshare.databinding.ActivityMainBinding;
import com.inhatc.tripshare.ui.HomeFragment;
import com.inhatc.tripshare.ui.MyItinerariesFragment;
import com.inhatc.tripshare.ui.ProfileFragment;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            showFragment(new HomeFragment());
            binding.bottomNav.setSelectedItemId(R.id.nav_home);
        }

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                showFragment(new HomeFragment());
                return true;
            } else if (id == R.id.nav_schedule) {
                showFragment(new MyItinerariesFragment());
                return true;
            } else if (id == R.id.nav_add) {
                startActivity(new Intent(this, CreateItineraryActivity.class));
                return false;
            } else if (id == R.id.nav_notifications) {
                Toast.makeText(this, R.string.notifications_coming, Toast.LENGTH_SHORT).show();
                return false;
            } else if (id == R.id.nav_profile) {
                showFragment(new ProfileFragment());
                return true;
            }
            return false;
        });

        binding.fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, CreateItineraryActivity.class)));
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
