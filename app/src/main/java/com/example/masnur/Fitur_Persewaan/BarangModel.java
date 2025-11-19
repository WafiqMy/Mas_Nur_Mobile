package com.example.masnur.Fitur_Persewaan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BarangModel implements Parcelable {
    @SerializedName("id_persewaan")
    private int idPersewaan;

    @SerializedName("nama_barang")
    private String namaBarang;

    @SerializedName("Jenis")
    private String jenis;

    @SerializedName("harga")
    private int harga;

    @SerializedName("jumlah")
    private int jumlah;

    @SerializedName("gambar")
    private String gambar;

    // Constructor kosong (wajib untuk Gson + Parcelable)
    public BarangModel() {}

    // Constructor lengkap (opsional, untuk testing)
    public BarangModel(int idPersewaan, String namaBarang, String jenis, int harga, int jumlah, String gambar) {
        this.idPersewaan = idPersewaan;
        this.namaBarang = namaBarang;
        this.jenis = jenis;
        this.harga = harga;
        this.jumlah = jumlah;
        this.gambar = gambar;
    }

    // Getter & Setter
    public int getIdPersewaan() { return idPersewaan; }
    public void setIdPersewaan(int idPersewaan) { this.idPersewaan = idPersewaan; }

    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public int getHarga() { return harga; }
    public void setHarga(int harga) { this.harga = harga; }

    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }

    public String getGambar() { return gambar; }
    public void setGambar(String gambar) { this.gambar = gambar; }

    // ✅ BAGIAN PENTING: Parcelable
    protected BarangModel(Parcel in) {
        idPersewaan = in.readInt();
        namaBarang = in.readString();
        jenis = in.readString();
        harga = in.readInt();
        jumlah = in.readInt();
        gambar = in.readString();
    }

    public static final Creator<BarangModel> CREATOR = new Creator<BarangModel>() {
        @Override
        public BarangModel createFromParcel(Parcel in) {
            return new BarangModel(in);
        }

        @Override
        public BarangModel[] newArray(int size) {
            return new BarangModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idPersewaan);
        dest.writeString(namaBarang);
        dest.writeString(jenis);
        dest.writeInt(harga);
        dest.writeInt(jumlah);
        dest.writeString(gambar);
    }
}