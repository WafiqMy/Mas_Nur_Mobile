package com.example.masnur.Fitur_Informasi_Masjid;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProfilMasjidModel implements Parcelable {
    @SerializedName("Id_profil_masjid")
    private int idProfilMasjid;

    @SerializedName("gambar_sejarah_masjid")
    private String gambarSejarahMasjid;

    @SerializedName("judul_sejarah")
    private String judulSejarah;

    @SerializedName("deskripsi_sejarah")
    private String deskripsiSejarah;

    @SerializedName("username")
    private String username;

    // Constructor kosong
    public ProfilMasjidModel() {}

    // Getter & Setter
    public int getIdProfilMasjid() { return idProfilMasjid; }
    public void setIdProfilMasjid(int idProfilMasjid) { this.idProfilMasjid = idProfilMasjid; }

    public String getGambarSejarahMasjid() { return gambarSejarahMasjid; }
    public void setGambarSejarahMasjid(String gambarSejarahMasjid) { this.gambarSejarahMasjid = gambarSejarahMasjid; }

    public String getJudulSejarah() { return judulSejarah; }
    public void setJudulSejarah(String judulSejarah) { this.judulSejarah = judulSejarah; }

    public String getDeskripsiSejarah() { return deskripsiSejarah; }
    public void setDeskripsiSejarah(String deskripsiSejarah) { this.deskripsiSejarah = deskripsiSejarah; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    // Parcelable
    protected ProfilMasjidModel(Parcel in) {
        idProfilMasjid = in.readInt();
        gambarSejarahMasjid = in.readString();
        judulSejarah = in.readString();
        deskripsiSejarah = in.readString();
        username = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idProfilMasjid);
        dest.writeString(gambarSejarahMasjid);
        dest.writeString(judulSejarah);
        dest.writeString(deskripsiSejarah);
        dest.writeString(username);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProfilMasjidModel> CREATOR = new Creator<ProfilMasjidModel>() {
        @Override
        public ProfilMasjidModel createFromParcel(Parcel in) {
            return new ProfilMasjidModel(in);
        }

        @Override
        public ProfilMasjidModel[] newArray(int size) {
            return new ProfilMasjidModel[size];
        }
    };
}