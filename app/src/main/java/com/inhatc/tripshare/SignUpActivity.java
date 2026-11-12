package com.inhatc.tripshare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.inhatc.tripshare.api.ApiCallback;
import com.inhatc.tripshare.api.TripRepository;
import com.inhatc.tripshare.databinding.ActivitySignupBinding;
import com.inhatc.tripshare.util.CaptchaViewHelper;

public class SignUpActivity extends BaseActivity {

    private ActivitySignupBinding binding;
    private CaptchaViewHelper captchaHelper;
    private final TripRepository repository = TripRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        captchaHelper = new CaptchaViewHelper(binding.captchaView.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSignup.setOnClickListener(v -> attemptSignUp());
    }

    private void attemptSignUp() {
        String email = getText(binding.etEmail);
        String password = getText(binding.etPassword);
        String confirm = getText(binding.etConfirmPassword);
        String nickname = getText(binding.etNickname);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirm) || TextUtils.isEmpty(nickname)) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirm)) {
            Toast.makeText(this, R.string.password_mismatch, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!captchaHelper.isVerified()) {
            Toast.makeText(this, R.string.captcha_wrong, Toast.LENGTH_SHORT).show();
            captchaHelper.refresh();
            return;
        }

        binding.btnSignup.setEnabled(false);
        repository.signUp(email, password, nickname, new ApiCallback<com.inhatc.tripshare.api.dto.UserDto>() {
            @Override
            public void onSuccess(com.inhatc.tripshare.api.dto.UserDto data) {
                binding.btnSignup.setEnabled(true);
                Toast.makeText(SignUpActivity.this, R.string.signup_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                binding.btnSignup.setEnabled(true);
                if ("conflict".equals(message)) {
                    Toast.makeText(SignUpActivity.this, R.string.email_exists, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
                captchaHelper.refresh();
            }
        });
    }

    private String getText(com.google.android.material.textfield.TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }
}
