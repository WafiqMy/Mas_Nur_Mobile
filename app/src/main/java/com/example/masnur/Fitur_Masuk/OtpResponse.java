package com.example.masnur.Fitur_Masuk;

public class OtpResponse {
    private String status;
    private String message;
    private String otp; // ← tambahkan field ini

    // Getter
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public String getOtp() { return otp; } // ← tambahkan getter ini
}