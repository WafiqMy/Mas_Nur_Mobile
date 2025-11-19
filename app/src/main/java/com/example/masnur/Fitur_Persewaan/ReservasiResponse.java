package com.example.masnur.Fitur_Persewaan;

import com.google.gson.annotations.SerializedName;

public class ReservasiResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("id_reservasi")
    private int idReservasi;

    @SerializedName("id_persewaan") // opsional, jika ada
    private int idPersewaan;

    // Constructor kosong (WAJIB untuk Gson)
    public ReservasiResponse() {}

    // Getter & Setter
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getIdReservasi() { return idReservasi; }
    public void setIdReservasi(int idReservasi) { this.idReservasi = idReservasi; }

    public int getIdPersewaan() { return idPersewaan; }
    public void setIdPersewaan(int idPersewaan) { this.idPersewaan = idPersewaan; }
}