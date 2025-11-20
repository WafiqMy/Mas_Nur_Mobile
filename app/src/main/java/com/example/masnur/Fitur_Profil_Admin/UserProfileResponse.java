package com.example.masnur.Fitur_Profil_Admin;

public class UserProfileResponse {
    private String status;
    private String message;
    private Data data;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    public static class Data {
        private String nama;
        private String role;
        private String gambar;

        public String getNama() { return nama; }
        public void setNama(String nama) { this.nama = nama; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getGambar() { return gambar; }
        public void setGambar(String gambar) { this.gambar = gambar; }
    }
}