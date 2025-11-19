package com.example.masnur.Fitur_Informasi_Masjid;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.Header_dan_Footer.Header;
import com.example.masnur.R;

public class Informasi_Masjid_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_masjid); // ✅ layout utama kamu

        Footer.setupFooter(this);
        Header.setupHeader(this);

        // Load fragment sejarah saat pertama buka
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new FragmentInformasiMasjid())
                    .commit();
        }

        Button btnSejarah = findViewById(R.id.btnSejarahhMasjid);
        Button btnStruktur = findViewById(R.id.btnStrukturOrganisasi);

        btnSejarah.setOnClickListener(v ->
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new FragmentInformasiMasjid())
                        .commit()
        );

        // Di Informasi_Masjid_Activity.java, ganti listener btnStrukturOrganisasi:
        btnStruktur.setOnClickListener(v ->
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new FragmentStrukturOrganisasi())
                        .commit()
        );
    }
}