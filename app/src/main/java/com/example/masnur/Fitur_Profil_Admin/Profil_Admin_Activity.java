package com.example.masnur.Fitur_Profil_Admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.Header_dan_Footer.Header;
import com.example.masnur.R;

public class Profil_Admin_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil); // pastikan nama file XML-nya benar
        Footer.setupFooter(this);
    }
}
