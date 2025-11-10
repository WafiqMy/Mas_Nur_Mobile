package com.example.masnur.Fitur_Masuk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.R;

public class SandiBaru extends AppCompatActivity {

    private EditText edtPasswordBaru, edtVerifikasiPassword;
    private Button btnLanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandi_baru);

        // Inisialisasi komponen
        edtPasswordBaru = findViewById(R.id.edtPasswordBaru);
        edtVerifikasiPassword = findViewById(R.id.edtVerifikasiPassword);
        btnLanjut = findViewById(R.id.btnLanjut);

        // Aksi saat tombol Lanjut ditekan
        btnLanjut.setOnClickListener(v -> {
            String passwordBaru = edtPasswordBaru.getText().toString().trim();
            String verifikasiPassword = edtVerifikasiPassword.getText().toString().trim();

            // Validasi input
            if (passwordBaru.isEmpty() || verifikasiPassword.isEmpty()) {
                Toast.makeText(this, "Isi semua kolom terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passwordBaru.equals(verifikasiPassword)) {
                Toast.makeText(this, "Kata sandi tidak sama, coba lagi!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Jika sukses
            Toast.makeText(this, "Kata sandi berhasil dibuat!", Toast.LENGTH_SHORT).show();

            // Pindah ke halaman login (Masuk)
            Intent intent = new Intent(SandiBaru.this, MasukActivity.class);
            startActivity(intent);
            finish(); // Tutup activity agar tidak bisa kembali ke halaman ini
        });
    }
}
