package com.example.masnur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Masuk extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk);

        etUsername = findViewById(R.id.edtUsername);
        etPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnMasuk);
        tvForgot = findViewById(R.id.tvLupaSandi);

        // Tombol Masuk
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Masuk.this, "Isi semua kolom terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else if (username.equals("admin") && password.equals("12345")) {
                    Toast.makeText(Masuk.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                    // Arahkan ke Beranda
                    Intent intent = new Intent(Masuk.this, Beranda.class);
                    startActivity(intent);
                    finish(); // agar tidak bisa kembali ke login
                } else {
                    Toast.makeText(Masuk.this, "Nama pengguna atau kata sandi salah", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Tombol "Lupa Kata Sandi"
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Arahkan ke halaman Konfirmasi Email
                Intent intent = new Intent(Masuk.this, KonfirmasiEmail.class);
                startActivity(intent);
                // Tidak perlu finish() agar user bisa kembali ke halaman login
            }
        });
    }
}
