package com.example.masnur.Fitur_Berita;
public class BeritaModel {
    private String id_berita;
    private String judul_berita;
    private String isi_berita;
    private String tanggal_berita;
    private String foto_berita;
    private String username;

    public BeritaModel(String id_berita, String judul_berita, String isi_berita,
                       String tanggal_berita, String foto_berita, String username) {
        this.id_berita = id_berita;
        this.judul_berita = judul_berita;
        this.isi_berita = isi_berita;
        this.tanggal_berita = tanggal_berita;
        this.foto_berita = foto_berita;
        this.username = username;
    }

    public String getIdBerita() { return id_berita; }
    public String getJudulBerita() { return judul_berita; }
    public String getIsiBerita() { return isi_berita; }
    public String getTanggalBerita() { return tanggal_berita; }
    public String getFotoBerita() { return foto_berita; }
    public String getUsername() { return username; }
}