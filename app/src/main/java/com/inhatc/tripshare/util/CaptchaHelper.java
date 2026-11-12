package com.inhatc.tripshare.util;

import java.util.Random;

public class CaptchaHelper {

    private final int num1;
    private final int num2;
    private final char operator;
    private final int answer;

    public CaptchaHelper() {
        Random random = new Random();
        num1 = random.nextInt(9) + 1;
        num2 = random.nextInt(9) + 1;
        operator = random.nextBoolean() ? '+' : '-';
        answer = operator == '+' ? num1 + num2 : num1 - num2;
    }

    public String getQuestion() {
        return num1 + " " + operator + " " + num2;
    }

    public boolean verify(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        try {
            return Integer.parseInt(input.trim()) == answer;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
