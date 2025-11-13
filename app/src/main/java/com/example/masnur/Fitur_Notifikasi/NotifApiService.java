package com.example.masnur.Fitur_Notifikasi;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NotifApiService {
    @GET("get_notifikasi.php")
    Call<String> getNotificationsRaw();
}
