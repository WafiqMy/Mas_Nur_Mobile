package com.example.masnur.Fitur_Acara;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AcaraModel implements Parcelable {
    @SerializedName("id_event") private String idEvent;
    @SerializedName("nama_event") private String namaEvent;
    @SerializedName("tanggal_event") private String tanggalEvent;
    @SerializedName("deskripsi_event") private String deskripsiEvent;
    @SerializedName("lokasi_event") private String lokasiEvent;
    @SerializedName("gambar_event") private String gambarEvent;
    @SerializedName("username") private String username;

    public AcaraModel() {}

    // Getter
    public String getIdEvent() { return idEvent; }
    public String getNamaEvent() { return namaEvent; }
    public String getTanggalEvent() { return tanggalEvent; }
    public String getDeskripsiEvent() { return deskripsiEvent; }
    public String getLokasiEvent() { return lokasiEvent; }
    public String getGambarEvent() { return gambarEvent; }
    public String getUsername() { return username; }

    // Setter (untuk mock)
    public void setIdEvent(String idEvent) { this.idEvent = idEvent; }
    public void setNamaEvent(String namaEvent) { this.namaEvent = namaEvent; }
    public void setTanggalEvent(String tanggalEvent) { this.tanggalEvent = tanggalEvent; }
    public void setGambarEvent(String gambarEvent) { this.gambarEvent = gambarEvent; }

    // Formatter tanggal: "2025-12-31 00:00:00" → "31 DEC 2025"
    public String getTanggalEventFormatted() {
        try {
            String input = getTanggalEvent();
            if (input == null || input.trim().isEmpty()) return "-";

            String datePart = input.split(" ")[0];
            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat out = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            Date date = in.parse(datePart);
            return out.format(date).toUpperCase();
        } catch (Exception e) {
            return getTanggalEvent();
        }
    }

    // Parcelable
    protected AcaraModel(Parcel in) {
        idEvent = in.readString();
        namaEvent = in.readString();
        tanggalEvent = in.readString();
        deskripsiEvent = in.readString();
        lokasiEvent = in.readString();
        gambarEvent = in.readString();
        username = in.readString();
    }

    public static final Creator<AcaraModel> CREATOR = new Creator<AcaraModel>() {
        @Override
        public AcaraModel createFromParcel(Parcel in) { return new AcaraModel(in); }
        @Override
        public AcaraModel[] newArray(int size) { return new AcaraModel[size]; }
    };

    @Override
    public int describeContents() { return 0; }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idEvent);
        dest.writeString(namaEvent);
        dest.writeString(tanggalEvent);
        dest.writeString(deskripsiEvent);
        dest.writeString(lokasiEvent);
        dest.writeString(gambarEvent);
        dest.writeString(username);
    }
}