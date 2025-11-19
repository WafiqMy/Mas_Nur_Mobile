package com.example.masnur.Fitur_Acara;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.Header_dan_Footer.Header;
import com.example.masnur.R;

public class Event_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acara); // pastikan nama file XML-nya benar
        Footer.setupFooter(this);
        Header.setupHeader(this);
    }
}