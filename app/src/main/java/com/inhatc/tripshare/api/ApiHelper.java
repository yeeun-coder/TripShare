package com.inhatc.tripshare.api;

import android.os.Handler;
import android.os.Looper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class ApiHelper {

    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    private ApiHelper() {
    }

    public static <T> void enqueue(Call<T> call, ApiCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MAIN.post(() -> callback.onSuccess(response.body()));
                } else {
                    MAIN.post(() -> callback.onError(parseError(response)));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                MAIN.post(() -> callback.onError(t.getMessage() != null
                        ? t.getMessage() : "network"));
            }
        });
    }

    private static <T> String parseError(Response<T> response) {
        int code = response.code();
        if (code == 401) {
            return "unauthorized";
        }
        if (code == 409) {
            return "conflict";
        }
        if (code == 404) {
            return "not_found";
        }
        return "error_" + code;
    }
}
