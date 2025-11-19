package com.example.masnur.Fitur_Informasi_Masjid;

import com.google.gson.annotations.SerializedName;

public class ProfilMasjidResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ProfilMasjidModel data;

    // Opsional: field tambahan dari API edit (id_profil, gambar_sejarah_masjid)
    @SerializedName("id_profil")
    private Integer idProfil;

    @SerializedName("gambar_sejarah_masjid")
    private String gambarSejarahMasjidFromEdit;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public ProfilMasjidModel getData() { return data; }
    public void setData(ProfilMasjidModel data) { this.data = data; }

    public Integer getIdProfil() { return idProfil; }
    public String getGambarSejarahMasjidFromEdit() { return gambarSejarahMasjidFromEdit; }
}