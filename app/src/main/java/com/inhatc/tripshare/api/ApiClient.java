package com.inhatc.tripshare.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private static TripShareApi api;

    private ApiClient() {
    }

    public static TripShareApi getApi() {
        if (api == null) {
            api = new Retrofit.Builder()
                    .baseUrl(ApiConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(TripShareApi.class);
        }
        return api;
    }
}
