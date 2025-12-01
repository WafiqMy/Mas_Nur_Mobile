package com.example.masnur.Fitur_Persewaan;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReservasiDetailResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Data data;

    // Getter
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public Data getData() { return data; }

    public static class Data {
        @SerializedName("id_reservasi")
        private int idReservasi;

        @SerializedName("nama_pengguna")
        private String namaPengguna;

        @SerializedName("no_tlp_pengguna")
        private String noTlpPengguna;

        @SerializedName("email_pengguna")
        private String emailPengguna;

        @SerializedName("nama_barang")
        private String namaBarang;

        @SerializedName("jenis")
        private String jenis;

        @SerializedName("total_peminjaman")
        private int totalPeminjaman;

        @SerializedName("total_harga")
        private int totalHarga;

        @SerializedName("keperluan")
        private String keperluan;

        @SerializedName("status_reservasi")
        private String statusReservasi;

        @SerializedName("notes")
        private String notes;

        // ✅ TANGGAL BARU — SESUAI DATABASE
        @SerializedName("tanggal_mulai_reservasi")
        private String tanggalMulaiReservasi;

        @SerializedName("tanggal_selesai_reservasi")
        private String tanggalSelesaiReservasi;

        // Getter
        public int getIdReservasi() { return idReservasi; }
        public String getNamaPengguna() { return namaPengguna; }
        public String getNoTlpPengguna() { return noTlpPengguna; }
        public String getEmailPengguna() { return emailPengguna; }
        public String getNamaBarang() { return namaBarang; }
        public String getJenis() { return jenis; }
        public int getTotalPeminjaman() { return totalPeminjaman; }
        public int getTotalHarga() { return totalHarga; }
        public String getKeperluan() { return keperluan; }
        public String getStatusReservasi() { return statusReservasi; }
        public String getNotes() { return notes; }

        // ✅ Getter Tanggal
        public String getTanggalMulaiReservasi() { return tanggalMulaiReservasi; }
        public String getTanggalSelesaiReservasi() { return tanggalSelesaiReservasi; }
    }
}