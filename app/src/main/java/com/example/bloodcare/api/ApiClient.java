package com.example.bloodcare.api;

import com.example.bloodcare.Config;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = Config.BASE_URL;
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Membuat HttpLoggingInterceptor untuk melacak request dan response
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Bisa menggunakan Level.BODY atau Level.HEADERS

            // Membuat OkHttpClient dengan interceptor
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // Menambahkan interceptor
                    .build();

            // Membuat Retrofit instance dengan OkHttpClient yang sudah ditambahkan interceptor
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)  // Menambahkan OkHttpClient
                    .addConverterFactory(GsonConverterFactory.create()) // Converter Gson untuk JSON
                    .build();
        }
        return retrofit;
    }
}
