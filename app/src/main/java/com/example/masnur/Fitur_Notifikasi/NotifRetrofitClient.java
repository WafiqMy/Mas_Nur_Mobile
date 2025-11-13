package com.example.masnur.Fitur_Notifikasi;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NotifRetrofitClient {

    private static final String BASE_URL = "http://masnurhuda.atwebpages.com/"; // WAJIB diakhiri '/'

    private static NotifApiService apiService;

    public static NotifApiService getApi() {
        if (apiService == null) {
            // Logging network biar gampang trace di Logcat (tag: NET)
            HttpLoggingInterceptor log = new HttpLoggingInterceptor(msg -> Log.d("NET", msg));
            log.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(log)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Gson lenient (lebih toleran, tapi tetap HARUS JSON valid dari server)
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    // taruh Scalars lebih dulu supaya kita bisa ambil raw String saat debug
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            apiService = retrofit.create(NotifApiService.class);
        }
        return apiService;
    }
}
