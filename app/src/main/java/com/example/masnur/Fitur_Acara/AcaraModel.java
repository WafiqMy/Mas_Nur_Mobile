package com.example.masnur.Fitur_Acara;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AcaraModel implements Parcelable {
    @SerializedName("id_event") private String idEvent;
    @SerializedName("nama_event") private String namaEvent;
    @SerializedName("tanggal_event") private String tanggalEvent;
    @SerializedName("deskripsi_event") private String deskripsiEvent;
    @SerializedName("lokasi_event") private String lokasiEvent;
    @SerializedName("gambar_event") private String gambarEvent;
    @SerializedName("dokumentasi") private String dokumentasiStr;
    @SerializedName("video_urls") private Object videoUrlsRaw;
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

    // Dokumentasi
    public List<String> getDokumentasi() {
        if (dokumentasiStr == null || dokumentasiStr.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String clean = dokumentasiStr.replace("[", "").replace("]", "").replace("\"", "");
        return Arrays.stream(clean.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    // ✅ ✅ ✅ VIDEO: Double-parse untuk handle nested JSON
    public String getVideoUrls() {
        if (videoUrlsRaw == null) return "";

        try {
            Gson gson = new Gson();
            String raw = "";

            // Langkah 1: Dapatkan string mentah
            if (videoUrlsRaw instanceof String) {
                raw = ((String) videoUrlsRaw).trim();
            } else if (videoUrlsRaw instanceof List) {
                List<?> list = (List<?>) videoUrlsRaw;
                if (!list.isEmpty() && list.get(0) instanceof String) {
                    raw = ((String) list.get(0)).trim();
                }
            }

            // Langkah 2: Jika string kelihatan seperti JSON array (misal: ["..."]), parse ulang
            if (raw.startsWith("[") && raw.endsWith("]")) {
                JsonElement element = JsonParser.parseString(raw);
                if (element.isJsonArray()) {
                    JsonArray arr = element.getAsJsonArray();
                    List<String> urls = new ArrayList<>();
                    for (JsonElement e : arr) {
                        if (e.isJsonPrimitive()) {
                            String url = e.getAsString().trim();
                            if (!url.isEmpty()) urls.add(url);
                        }
                    }
                    return TextUtils.join(", ", urls);
                }
            }

            // Jika bukan array valid, kembalikan string asli (hapus tanda [""] jika ada)
            return raw
                    .replace("\\\"", "\"")
                    .replace("[\"", "")
                    .replace("\"]", "")
                    .replace("\"", "")
                    .trim();

        } catch (Exception e) {
            // Jika error parsing, kembalikan raw
            if (videoUrlsRaw instanceof String) {
                return ((String) videoUrlsRaw).trim();
            } else {
                return videoUrlsRaw != null ? videoUrlsRaw.toString().trim() : "";
            }
        }
    }

    // Gambar utama
    public String getGambarEventAbsolut() {
        String url = getGambarEvent();
        if (url == null || url.trim().isEmpty() || "default.jpg".equals(url.trim())) {
            return "https://via.placeholder.com/600x300/e0e0e0/999999?text=No+Image";
        }
        if (url.startsWith("http")) return url.trim();
        String BASE = "https://masnurhudanganjuk.pbltifnganjuk.com/API/uploads/kegiatan/";
        return BASE + url.trim();
    }

    // Format tanggal
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

    // === PARCELABLE ===
    protected AcaraModel(Parcel in) {
        idEvent = in.readString();
        namaEvent = in.readString();
        tanggalEvent = in.readString();
        deskripsiEvent = in.readString();
        lokasiEvent = in.readString();
        gambarEvent = in.readString();
        dokumentasiStr = in.readString();
        videoUrlsRaw = in.readString(); // simpan sebagai string
        username = in.readString();
    }

    public static final Creator<AcaraModel> CREATOR = new Creator<AcaraModel>() {
        @Override
        public AcaraModel createFromParcel(Parcel in) {
            return new AcaraModel(in);
        }

        @Override
        public AcaraModel[] newArray(int size) {
            return new AcaraModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idEvent);
        dest.writeString(namaEvent);
        dest.writeString(tanggalEvent);
        dest.writeString(deskripsiEvent);
        dest.writeString(lokasiEvent);
        dest.writeString(gambarEvent);
        dest.writeString(dokumentasiStr);
        dest.writeString(videoUrlsRaw != null ? videoUrlsRaw.toString() : "");
        dest.writeString(username);
    }
}