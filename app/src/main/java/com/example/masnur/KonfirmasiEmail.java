package com.example.masnur;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class KonfirmasiEmail extends AppCompatActivity {

    EditText edtEmail;
    Button buttonLanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_email);

        // Inisialisasi komponen
        edtEmail = findViewById(R.id.edtEmail);
        buttonLanjut = findViewById(R.id.buttonLanjut);

        // Tombol "Lanjut"
        buttonLanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(KonfirmasiEmail.this, "Masukkan email terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                // Cek format email dengan regex bawaan Android
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(KonfirmasiEmail.this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Jika format valid
                    Toast.makeText(KonfirmasiEmail.this, "Kode verifikasi telah dikirim ke email: " + email, Toast.LENGTH_LONG).show();

                    // Pindah ke halaman Kode OTP
                    Intent intent = new Intent(KonfirmasiEmail.this, KodeOtp.class);
                    startActivity(intent);
                }
            }
        });
    }
}
