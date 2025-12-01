package com.example.masnur.Fitur_Berita;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Fitur_Halaman_Utama.Halaman_Utama_Activity;
import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.R;

public class Berita_Activity extends AppCompatActivity {
    Button btnBerita, btnKelola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);

        Footer.setupFooter(this);

        btnBerita = findViewById(R.id.btnFberita);
        btnKelola = findViewById(R.id.btnFkelolaberita);

        ImageView btnKembali = findViewById(R.id.btnKembali);

        btnKembali.setOnClickListener(v -> {
            Intent intent = new Intent(Berita_Activity.this, Halaman_Utama_Activity.class);
            startActivity(intent);
            finish(); // supaya tidak kembali ke Informasi_Masjid_Activity lagi
        });

        btnBerita.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new BeritaFragment())
                    .commit();
        });

        btnKelola.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new KelolaBeritaFragment())
                    .commit();
        });

        // ✅ Tampilkan fragment default saat activity dibuka
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new BeritaFragment())
                    .commit();
        }
    }
}