package com.example.masnur.Fitur_Berita;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.Header_dan_Footer.Header;
import com.example.masnur.R;

public class Berita_Activity extends AppCompatActivity {
    Button btnBerita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita);

        Footer.setupFooter(this);
        Header.setupHeader(this);

        btnBerita = findViewById(R.id.btnFberita);

        btnBerita.setOnClickListener(v -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, new BeritaFragment());
            transaction.commit();
        });
    }
}