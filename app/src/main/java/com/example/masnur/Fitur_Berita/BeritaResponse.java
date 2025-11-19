package com.example.masnur.Fitur_Berita;

import com.google.gson.annotations.SerializedName;

public class BeritaResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("id_berita")
    private int idBerita;

    @SerializedName("foto_berita")
    private String fotoBerita;

    // Constructor kosong
    public BeritaResponse() {}

    // Getter
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public int getIdBerita() { return idBerita; }
    public String getFotoBerita() { return fotoBerita; }
}