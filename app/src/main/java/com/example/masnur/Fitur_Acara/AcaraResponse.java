package com.example.masnur.Fitur_Acara;

import com.google.gson.annotations.SerializedName;

public class AcaraResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    // Gunakan salah satu sesuai struktur JSON dari API
    @SerializedName("id_event") // atau "id_acara"
    private String idEvent;

    @SerializedName("gambar_event") // atau "poster"
    private String gambarEvent;

    // Getter
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public String getGambarEvent() {
        return gambarEvent;
    }
}