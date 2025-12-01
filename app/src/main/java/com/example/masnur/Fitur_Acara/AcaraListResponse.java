package com.example.masnur.Fitur_Acara;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AcaraListResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<AcaraModel> data;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<AcaraModel> getData() {
        return data;
    }
}