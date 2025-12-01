// File: app/src/main/java/com/example/masnur/Fitur_Persewaan/ReservasiItemModel.java

package com.example.masnur.Fitur_Persewaan;

import com.google.gson.annotations.SerializedName;

public class ReservasiItemModel {
    private int id;
    private String title;
    private String start;
    private String end;
    private String color;

    @SerializedName("extendedProps")
    private ExtendedProps extendedProps;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getStart() { return start; }
    public String getEnd() { return end; }
    public String getColor() { return color; }
    public ExtendedProps getExtendedProps() { return extendedProps; }

    public static class ExtendedProps {
        private String peminjam;
        private String telepon;
        private String email;
        private String barang;
        private String jenis;
        private int jumlah;
        private int harga;
        private String keperluan;
        private String status;
        private String notes;

        public String getPeminjam() { return peminjam; }
        public String getTelepon() { return telepon; }
        public String getEmail() { return email; }
        public String getBarang() { return barang; }
        public String getJenis() { return jenis; }
        public int getJumlah() { return jumlah; }
        public int getHarga() { return harga; }
        public String getKeperluan() { return keperluan; }
        public String getStatus() { return status; }
        public String getNotes() { return notes; }
    }
}