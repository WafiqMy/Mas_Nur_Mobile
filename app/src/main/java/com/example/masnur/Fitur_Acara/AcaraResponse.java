package com.example.masnur.Fitur_Acara;

import com.google.gson.annotations.SerializedName;

public class AcaraResponse {
    @SerializedName("status") private String status;
    @SerializedName("message") private String message;
    @SerializedName("id_event") private String idEvent;
    @SerializedName("gambar_event") private String gambarEvent;

    // Getter
    public String getStatus() { return status != null ? status : ""; }
    public String getMessage() { return message != null ? message : ""; }
    public String getIdEvent() { return idEvent; }
    public String getGambarEvent() { return gambarEvent; }

    // ✅ TAMBAHKAN METHOD INI — untuk cek apakah sukses
    public boolean isSuccess() {
        return "success".equals(status) || "1".equals(status);
    }
}