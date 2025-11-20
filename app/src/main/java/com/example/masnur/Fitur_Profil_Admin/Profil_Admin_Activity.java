package com.example.masnur.Fitur_Profil_Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.R;

public class Profil_Admin_Activity extends AppCompatActivity {

    private Button btnGantiNama;
    private Button btnGantiSandi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_admin); // pastikan nama file XML-nya benar

        Footer.setupFooter(this);

        // Inisialisasi tombol
        btnGantiNama = findViewById(R.id.btn_ganti_nama);
        btnGantiSandi = findViewById(R.id.btn_ganti_sandi);

        // Aksi tombol
        btnGantiNama.setOnClickListener(v -> {
            Intent intent = new Intent(Profil_Admin_Activity.this, GantiNamaActivity.class);
            startActivityForResult(intent, 101);
        });

        btnGantiSandi.setOnClickListener(v -> {
            Intent intent = new Intent(Profil_Admin_Activity.this, GantiKataSandiActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            loadUserProfile();
        }
    }

    private void loadUserProfile() {
        // Tambahkan logika untuk memuat ulang profil admin di sini
    }
}