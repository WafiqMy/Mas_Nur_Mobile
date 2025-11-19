package com.example.masnur.Fitur_Notifikasi;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NotifResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<NotificationItem> data;

    // Constructor kosong untuk Gson
    public NotifResponse() {}

    // Getter
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<NotificationItem> getData() {
        return data;
    }

    // Setter (opsional)
    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<NotificationItem> data) {
        this.data = data;
    }
}