package com.example.masnur.Fitur_Informasi_Masjid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Fitur_Halaman_Utama.Halaman_Utama_Activity;
import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.R;

public class Informasi_Masjid_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_masjid); // ✅ layout utama

        // Footer tetap dipakai, header sudah dihapus dari layout
        Footer.setupFooter(this);

        // Tombol kembali
        ImageView btnKembali = findViewById(R.id.btnKembali);
        btnKembali.setOnClickListener(v -> {
            Intent intent = new Intent(Informasi_Masjid_Activity.this, Halaman_Utama_Activity.class);
            startActivity(intent);
            finish(); // supaya tidak kembali ke Informasi_Masjid_Activity lagi
        });

        // Load fragment sejarah saat pertama buka
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new FragmentInformasiMasjid())
                    .commit();
        }

        // Tombol navigasi antar fragment
        Button btnSejarah = findViewById(R.id.btnSejarahMasjid);
        Button btnStruktur = findViewById(R.id.btnStrukturOrganisasi);

        btnSejarah.setOnClickListener(v ->
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new FragmentInformasiMasjid())
                        .commit()
        );

        btnStruktur.setOnClickListener(v ->
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new FragmentStrukturOrganisasi())
                        .commit()
        );
    }
}