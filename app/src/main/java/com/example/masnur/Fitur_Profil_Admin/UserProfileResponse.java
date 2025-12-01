package com.example.masnur.Fitur_Profil_Admin;

public class UserProfileResponse {
    private String status;
    private String message;
    private Data data;

    public static class Data {
        private String username;
        private String nama; // ← ini dari kolom 'nama' di DB
        private String email;

        public String getUsername() { return username; }
        public String getNama() { return nama; } // ✅
        public String getEmail() { return email; }
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public Data getData() { return data; }
}