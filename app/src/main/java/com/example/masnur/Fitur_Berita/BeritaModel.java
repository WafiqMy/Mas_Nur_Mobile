package com.example.masnur.Fitur_Berita;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BeritaModel implements Parcelable {

    @SerializedName("id_berita") private String idBerita;
    @SerializedName("judul_berita") private String judulBerita;
    @SerializedName("isi_berita") private String isiBerita;
    @SerializedName("tanggal_berita") private String tanggalBerita;
    @SerializedName("foto_berita") private String fotoBerita;
    @SerializedName("username") private String username;

    public BeritaModel() {}

    public BeritaModel(String idBerita, String judulBerita, String isiBerita,
                       String tanggalBerita, String fotoBerita, String username) {
        this.idBerita = idBerita;
        this.judulBerita = judulBerita;
        this.isiBerita = isiBerita;
        this.tanggalBerita = tanggalBerita;
        this.fotoBerita = fotoBerita;
        this.username = username;
    }

    public String getIdBerita() { return idBerita; }
    public void setIdBerita(String idBerita) { this.idBerita = idBerita; }

    public String getJudulBerita() { return judulBerita; }
    public void setJudulBerita(String judulBerita) { this.judulBerita = judulBerita; }

    public String getIsiBerita() { return isiBerita; }
    public void setIsiBerita(String isiBerita) { this.isiBerita = isiBerita; }

    public String getTanggalBerita() { return tanggalBerita; }
    public void setTanggalBerita(String tanggalBerita) { this.tanggalBerita = tanggalBerita; }

    public String getFotoBerita() { return fotoBerita; }
    public void setFotoBerita(String fotoBerita) { this.fotoBerita = fotoBerita; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // ✅ TAMBAHKAN METHOD INI
    public String getFotoBeritaAbsolut() {
        String url = getFotoBerita();
        if (url == null || url.trim().isEmpty() || "default.jpg".equals(url)) {
            return "https://via.placeholder.com/600x300/e0e0e0/999999?text=No+Image";
        }
        return url;
    }

    // Parcelable
    protected BeritaModel(Parcel in) {
        idBerita = in.readString();
        judulBerita = in.readString();
        isiBerita = in.readString();
        tanggalBerita = in.readString();
        fotoBerita = in.readString();
        username = in.readString();
    }

    public static final Creator<BeritaModel> CREATOR = new Creator<BeritaModel>() {
        @Override
        public BeritaModel createFromParcel(Parcel in) { return new BeritaModel(in); }
        @Override
        public BeritaModel[] newArray(int size) { return new BeritaModel[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idBerita);
        dest.writeString(judulBerita);
        dest.writeString(isiBerita);
        dest.writeString(tanggalBerita);
        dest.writeString(fotoBerita);
        dest.writeString(username);
    }
}