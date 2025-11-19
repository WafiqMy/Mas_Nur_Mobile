package com.example.masnur.Fitur_Notifikasi;

import com.google.gson.annotations.SerializedName;

public class NotificationItem {

    @SerializedName("id_reservasi")
    private String id_reservasi;

    @SerializedName("nama_pengguna")
    private String nama_pengguna;

    @SerializedName("jenis")
    private String jenis;

    // Constructor kosong untuk Gson
    public NotificationItem() {}

    // Getter
    public String getId_reservasi() {
        return id_reservasi;
    }

    public String getNama_pengguna() {
        return nama_pengguna;
    }

    public String getJenis() {
        return jenis;
    }

    // Setter (opsional)
    public void setId_reservasi(String id_reservasi) {
        this.id_reservasi = id_reservasi;
    }

    public void setNama_pengguna(String nama_pengguna) {
        this.nama_pengguna = nama_pengguna;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}