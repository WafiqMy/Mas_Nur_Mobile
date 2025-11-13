package com.example.masnur.Fitur_Halaman_Utama;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.Header_dan_Footer.Header;
import com.example.masnur.R;

public class Halaman_Utama_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama); // pastikan nama file XML-nya benar

        Footer.setupFooter(this);
        Header.setupHeader(this);
    }
}