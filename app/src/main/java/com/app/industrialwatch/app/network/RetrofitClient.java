package com.app.industrialwatch.app.network;

import com.app.industrialwatch.common.utils.AppConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.HOURS)
                .connectTimeout(1,TimeUnit.HOURS)
                .writeTimeout(1,TimeUnit.HOURS)
                .addInterceptor(logging).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
