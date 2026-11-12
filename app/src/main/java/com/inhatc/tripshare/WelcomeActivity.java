package com.inhatc.tripshare;

import android.content.Intent;
import android.os.Bundle;

import com.inhatc.tripshare.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends BaseActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));

        binding.btnSignup.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
    }
}
