package com.example.masnur.Fitur_Berita;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.masnur.R;

public class DetailBeritaActivity extends AppCompatActivity {

    private ImageView imageDetailBerita;
    private TextView textJudulDetail, textTanggalDetail, textIsiDetail;
    private BeritaModel berita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);

        // Inisialisasi view
        imageDetailBerita = findViewById(R.id.imageDetailBerita);
        textJudulDetail = findViewById(R.id.textJudulDetail);
        textTanggalDetail = findViewById(R.id.textTanggalDetail);
        textIsiDetail = findViewById(R.id.textIsiDetail);

        // Ambil data berita dari intent
        berita = getIntent().getParcelableExtra("berita");
        if (berita == null) {
            finish(); // Jika tidak ada data, tutup activity
            return;
        }

        // Set data ke view
        textJudulDetail.setText(berita.getJudulBerita());
        textTanggalDetail.setText(berita.getTanggalBerita()); // Format bisa disesuaikan
        textIsiDetail.setText(berita.getIsiBerita());

        // Load gambar dengan Glide
        String imageUrl = berita.getFotoBeritaAbsolut();
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(imageDetailBerita);

        // Setup tombol kembali
        findViewById(R.id.btnKembali).setOnClickListener(v -> finish());
    }
}