package com.example.masnur.Fitur_Informasi_Masjid;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StrukturOrganisasiModel implements Parcelable {
    @SerializedName("Id_profil_masjid")
    private int idProfilMasjid;

    @SerializedName("gambar_struktur_organisasi")
    private String gambarStrukturOrganisasi;

    @SerializedName("gambar_struktur_remas")
    private String gambarStrukturRemas;

    // ✅ TAMBAHKAN FIELD URL ABSOLUT
    @SerializedName("gambar_struktur_organisasi_url")
    private String gambarStrukturOrganisasiUrl;

    @SerializedName("gambar_struktur_remas_url")
    private String gambarStrukturRemasUrl;

    public StrukturOrganisasiModel() {}

    // Getter & Setter
    public int getIdProfilMasjid() { return idProfilMasjid; }
    public void setIdProfilMasjid(int idProfilMasjid) { this.idProfilMasjid = idProfilMasjid; }

    public String getGambarStrukturOrganisasi() { return gambarStrukturOrganisasi; }
    public void setGambarStrukturOrganisasi(String gambarStrukturOrganisasi) { this.gambarStrukturOrganisasi = gambarStrukturOrganisasi; }

    public String getGambarStrukturRemas() { return gambarStrukturRemas; }
    public void setGambarStrukturRemas(String gambarStrukturRemas) { this.gambarStrukturRemas = gambarStrukturRemas; }

    // ✅ Getter & Setter URL
    public String getGambarStrukturOrganisasiUrl() { return gambarStrukturOrganisasiUrl; }
    public void setGambarStrukturOrganisasiUrl(String gambarStrukturOrganisasiUrl) { this.gambarStrukturOrganisasiUrl = gambarStrukturOrganisasiUrl; }

    public String getGambarStrukturRemasUrl() { return gambarStrukturRemasUrl; }
    public void setGambarStrukturRemasUrl(String gambarStrukturRemasUrl) { this.gambarStrukturRemasUrl = gambarStrukturRemasUrl; }

    // Parcelable
    protected StrukturOrganisasiModel(Parcel in) {
        idProfilMasjid = in.readInt();
        gambarStrukturOrganisasi = in.readString();
        gambarStrukturRemas = in.readString();
        gambarStrukturOrganisasiUrl = in.readString(); // ✅
        gambarStrukturRemasUrl = in.readString();       // ✅
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idProfilMasjid);
        dest.writeString(gambarStrukturOrganisasi);
        dest.writeString(gambarStrukturRemas);
        dest.writeString(gambarStrukturOrganisasiUrl); // ✅
        dest.writeString(gambarStrukturRemasUrl);       // ✅
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StrukturOrganisasiModel> CREATOR = new Creator<StrukturOrganisasiModel>() {
        @Override
        public StrukturOrganisasiModel createFromParcel(Parcel in) {
            return new StrukturOrganisasiModel(in);
        }

        @Override
        public StrukturOrganisasiModel[] newArray(int size) {
            return new StrukturOrganisasiModel[size];
        }
    };
}