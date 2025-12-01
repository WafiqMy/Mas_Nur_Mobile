// File: com.example.masnur.Fitur_Persewaan.Persewaan_Activity.java

package com.example.masnur.Fitur_Persewaan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.masnur.Fitur_Halaman_Utama.Halaman_Utama_Activity;
import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.R;

public class Persewaan_Activity extends AppCompatActivity {
    Button btnPemesanan, btnKelola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);
        Footer.setupFooter(this);

        btnPemesanan = findViewById(R.id.btnPemesanan);
        btnKelola = findViewById(R.id.btnKelolaPemesanan);
        ImageView btnKembali = findViewById(R.id.btnKembali);

        btnKembali.setOnClickListener(v -> {
            Intent intent = new Intent(Persewaan_Activity.this, Halaman_Utama_Activity.class);
            startActivity(intent);
            finish();
        });

        // 🔴 DEFAULT: tampilkan PemesananFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new PemesananFragment())
                    .commit();
        }

        btnPemesanan.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new PemesananFragment())
                    .commit();
        });

        btnKelola.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new FragmentKelolaPersewaan())
                    .addToBackStack(null)
                    .commit();
        });
    }
}