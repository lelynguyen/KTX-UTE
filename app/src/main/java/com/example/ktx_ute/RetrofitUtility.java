package com.example.ktx_ute;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitUtility {
    private static final String SERVICE_URL = "https://spaceofme.000webhostapp.com/api_ktx/";
    private static Retrofit retrofit;

    private static Retrofit getRetrofit(String url) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static <T> T getService(Class<T> serviceClass) {
        return getRetrofit(SERVICE_URL).create(serviceClass);
    }
}
