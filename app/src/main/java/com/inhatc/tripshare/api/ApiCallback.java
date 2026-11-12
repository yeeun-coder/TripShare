package com.inhatc.tripshare.api;

public interface ApiCallback<T> {
    void onSuccess(T data);

    void onError(String message);
}
