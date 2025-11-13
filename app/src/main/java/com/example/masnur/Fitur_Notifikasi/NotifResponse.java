package com.example.masnur.Fitur_Notifikasi;
import com.example.masnur.Fitur_Notifikasi.NotificationItem;

import java.util.List;

public class NotifResponse {
    private String status;
    private String message;
    private List<NotificationItem> data;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public List<NotificationItem> getData() { return data; }
}