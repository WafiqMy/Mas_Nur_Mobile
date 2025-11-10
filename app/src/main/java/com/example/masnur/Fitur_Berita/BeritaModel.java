package com.example.masnur.Fitur_Berita;

public class BeritaModel {
    private String id;
    private String judul;
    private String isi;
    private String tanggal;
    private String foto;
    private String username;

    public BeritaModel(String id, String judul, String isi, String tanggal, String foto, String username) {
        this.id = id;
        this.judul = judul;
        this.isi = isi;
        this.tanggal = tanggal;
        this.foto = foto;
        this.username = username;
    }

    public String getId() { return id; }
    public String getJudul() { return judul; }
    public String getIsi() { return isi; }
    public String getTanggal() { return tanggal; }
    public String getFoto() { return foto; }
    public String getUsername() { return username; }
}