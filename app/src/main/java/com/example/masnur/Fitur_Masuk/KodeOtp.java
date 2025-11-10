package com.example.masnur.Fitur_Masuk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.R;

public class KodeOtp extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4;
    private Button buttonLanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kode_otp);

        // Inisialisasi komponen
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        buttonLanjut = findViewById(R.id.buttonLanjut);

        // Saat tombol diklik
        buttonLanjut.setOnClickListener(v -> {
            String kodeOTP = otp1.getText().toString().trim() +
                    otp2.getText().toString().trim() +
                    otp3.getText().toString().trim() +
                    otp4.getText().toString().trim();

            if (kodeOTP.length() < 4) {
                Toast.makeText(this, "Lengkapi semua kolom OTP!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Contoh verifikasi: OTP benar jika "1234"
            if (kodeOTP.equals("1234")) {
                Toast.makeText(this, "Verifikasi berhasil!", Toast.LENGTH_SHORT).show();

                // Arahkan ke halaman Buat Kata Sandi Baru
                Intent intent = new Intent(KodeOtp.this, SandiBaru.class);
                startActivity(intent);
                finish(); // Tutup halaman OTP agar tidak bisa kembali ke sini

            } else {
                Toast.makeText(this, "Kode OTP salah, coba lagi!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
