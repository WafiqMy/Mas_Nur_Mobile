package com.example.masnur.Fitur_Persewaan;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.Header_dan_Footer.Header;
import com.example.masnur.R;

public class Pemesanan_Activity extends AppCompatActivity {
    Button btnPemesanan, btnKelola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);
        Footer.setupFooter(this);
        Header.setupHeader(this);

        btnPemesanan = findViewById(R.id.btnPemesanan);
        btnKelola = findViewById(R.id.btnKelolaPemesanan);

//        // Default: tampilkan fragment pemesanan
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.frameLayout, new PemesananFragment()) // Ganti dengan fragment pemesanan kamu
//                    .commit();
//        }

        // Tombol Kelola Pemesanan → ganti ke KelolaPersewaanFragment
        btnKelola.setOnClickListener(v -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new FragmentKelolaPersewaan());
            ft.addToBackStack(null); // opsional: tekan back → kembali ke sebelumnya
            ft.commit();
        });


    }
}