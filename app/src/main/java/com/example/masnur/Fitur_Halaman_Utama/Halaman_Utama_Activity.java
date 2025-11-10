package com.example.masnur.Fitur_Halaman_Utama;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;
import com.example.masnur.Header_dan_Footer.Footer;

import com.example.masnur.R;
import com.example.masnur.Fitur_Profil_Admin.Profil_Admin_Activity;
import com.example.masnur.Fitur_Notifikasi.Notifikasi_Activity;

public class Halaman_Utama_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama); // pastikan nama file XML-nya benar

        Footer.setupFooter(this);

        ImageView iconProfil = findViewById(R.id.iconProfil);
        ImageView iconNotifikasi = findViewById(R.id.iconNotifikasi);

        iconProfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, Profil_Admin_Activity.class);
            startActivity(intent);
        });

        iconNotifikasi.setOnClickListener(v -> {
            Intent intent = new Intent(this, Notifikasi_Activity.class);
            startActivity(intent);
        });
    }
}