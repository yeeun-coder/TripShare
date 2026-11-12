package com.inhatc.tripshare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.inhatc.tripshare.api.ApiCallback;
import com.inhatc.tripshare.api.TripRepository;
import com.inhatc.tripshare.databinding.ActivityLoginBinding;
import com.inhatc.tripshare.util.CaptchaViewHelper;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private CaptchaViewHelper captchaHelper;
    private final TripRepository repository = TripRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        captchaHelper = new CaptchaViewHelper(binding.captchaView.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());
        binding.tvSignup.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
        binding.btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = binding.etEmail.getText() != null
                ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null
                ? binding.etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!captchaHelper.isVerified()) {
            Toast.makeText(this, R.string.captcha_wrong, Toast.LENGTH_SHORT).show();
            captchaHelper.refresh();
            return;
        }

        binding.btnLogin.setEnabled(false);
        repository.login(email, password, new ApiCallback<com.inhatc.tripshare.api.dto.UserDto>() {
            @Override
            public void onSuccess(com.inhatc.tripshare.api.dto.UserDto data) {
                binding.btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                binding.btnLogin.setEnabled(true);
                if ("unauthorized".equals(message)) {
                    Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
                captchaHelper.refresh();
            }
        });
    }
}
