package com.example.masnur.Fitur_Berita;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.Header_dan_Footer.Header;
import com.example.masnur.R;

public class Berita_Activity extends AppCompatActivity {
    Button btnBerita, btnKelola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);

        Footer.setupFooter(this);
        Header.setupHeader(this);

        btnBerita = findViewById(R.id.btnFberita);
        btnKelola = findViewById(R.id.btnFkelolaberita);

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