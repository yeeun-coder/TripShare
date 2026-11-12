package com.inhatc.tripshare.util;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.inhatc.tripshare.R;

public class CaptchaViewHelper {

    private final TextView tvQuestion;
    private final TextInputEditText etAnswer;
    private CaptchaHelper captcha;

    public CaptchaViewHelper(View root) {
        tvQuestion = root.findViewById(R.id.tv_captcha_question);
        etAnswer = root.findViewById(R.id.et_captcha_answer);
        ImageButton btnRefresh = root.findViewById(R.id.btn_captcha_refresh);
        btnRefresh.setOnClickListener(v -> refresh());
        refresh();
    }

    public void refresh() {
        captcha = new CaptchaHelper();
        tvQuestion.setText(captcha.getQuestion() + " = ?");
        if (etAnswer.getText() != null) {
            etAnswer.getText().clear();
        }
    }

    public boolean isVerified() {
        String answer = etAnswer.getText() != null ? etAnswer.getText().toString() : "";
        return captcha.verify(answer);
    }
}
