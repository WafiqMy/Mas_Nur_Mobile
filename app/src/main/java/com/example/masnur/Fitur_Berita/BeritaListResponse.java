package com.example.masnur.Fitur_Berita;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BeritaListResponse {
    @SerializedName("status") private String status;
    @SerializedName("message") private String message;
    @SerializedName("data") private List<BeritaModel> data;

    public String getStatus() { return status != null ? status : ""; }
    public String getMessage() { return message != null ? message : ""; }
    public List<BeritaModel> getData() { return data; }

    public boolean isSuccess() {
        return "success".equals(status) || "1".equals(status);
    }
}