package com.example.masnur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class Dashboard_Activity extends AppCompatActivity {

    private ImageView headerImage, masjidLogo;
    private TextView userName, userRole;
    private Button btnGantiNama, btnGantiSandi, btnKeluar;
    private LinearLayout footerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        headerImage = findViewById(R.id.header_image);
        masjidLogo = findViewById(R.id.masjid_logo);
        userName = findViewById(R.id.user_name);
        userRole = findViewById(R.id.user_role);
        btnGantiNama = findViewById(R.id.btn_ganti_nama);
        btnGantiSandi = findViewById(R.id.btn_ganti_sandi);
        btnKeluar = findViewById(R.id.btn_keluar);
        footerContainer = findViewById(R.id.footer_container);

        // Contoh data profil (bisa diganti dari SharedPreferences / Database)
        userName.setText("M. Wafiq Marzuq Yuwono");
        userRole.setText("Marbot Masjid");

        btnGantiNama.setOnClickListener(v -> {
            Toast.makeText(this, "Menu Ganti Nama diklik", Toast.LENGTH_SHORT).show();

        });

        // Aksi tombol Ganti Kata Sandi
        btnGantiSandi.setOnClickListener(v -> {
            Toast.makeText(this, "Menu Ganti Kata Sandi diklik", Toast.LENGTH_SHORT).show();

        });

        btnKeluar.setOnClickListener(v -> {
            Toast.makeText(this, "Anda telah keluar", Toast.LENGTH_SHORT).show();

        });
    }
}
