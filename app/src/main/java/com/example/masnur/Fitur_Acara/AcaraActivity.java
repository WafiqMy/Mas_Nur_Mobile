package com.example.masnur.Fitur_Acara;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.Header_dan_Footer.Header;
import com.example.masnur.R;

public class AcaraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acara);

        // Setup header & footer
        Header.setupHeader(this);
        Footer.setupFooter(this);

        // ✅ Hanya load fragment jika belum ada state sebelumnya
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new LihatAcaraFragment()) // ✅ ID harus SAMA
                    .commit();
        }
    }
}