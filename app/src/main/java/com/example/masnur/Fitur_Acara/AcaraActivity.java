package com.example.masnur.Fitur_Acara;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Fitur_Halaman_Utama.Halaman_Utama_Activity;
import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.R;

public class AcaraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acara);

        // Tombol kembali
        ImageView btnKembali = findViewById(R.id.btnKembali);

        // Setup footer (header sudah dihapus dari layout)
        Footer.setupFooter(this);

        // ✅ Hanya load fragment jika belum ada state sebelumnya
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new LihatAcaraFragment()) // ✅ ID harus sama dengan XML
                    .commit();
        }

        // Listener tombol kembali
        btnKembali.setOnClickListener(v -> {
            Intent intent = new Intent(AcaraActivity.this, Halaman_Utama_Activity.class);
            startActivity(intent);
            finish(); // supaya tidak kembali ke AcaraActivity lagi
        });
    }
}