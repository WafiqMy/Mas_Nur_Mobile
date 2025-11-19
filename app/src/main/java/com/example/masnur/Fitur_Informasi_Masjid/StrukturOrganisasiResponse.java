// com.example.masnur.Fitur_Informasi_Masjid.StrukturOrganisasiResponse.java
package com.example.masnur.Fitur_Informasi_Masjid;

import com.google.gson.annotations.SerializedName;

public class StrukturOrganisasiResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private StrukturOrganisasiModel data;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public StrukturOrganisasiModel getData() { return data; }
    public void setData(StrukturOrganisasiModel data) { this.data = data; }
}