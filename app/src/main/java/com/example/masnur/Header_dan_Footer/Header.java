package com.example.masnur.Header_dan_Footer;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.masnur.R;
import com.example.masnur.Fitur_Profil_Admin.Profil_Admin_Activity;
import com.example.masnur.Fitur_Notifikasi.Notifikasi_Activity;

public class Header {

    public static View getHeader(Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View headerView = inflater.inflate(R.layout.header, null);

        ImageView iconProfil = headerView.findViewById(R.id.iconProfil);
        ImageView iconNotifikasi = headerView.findViewById(R.id.iconNotifikasi);

        // 🔵 Aksi tombol Profil
        iconProfil.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Profil_Admin_Activity.class);
            activity.startActivity(intent);
        });

        // 🔔 Aksi tombol Notifikasi
        iconNotifikasi.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Notifikasi_Activity.class);
            activity.startActivity(intent);
        });

        return headerView;
    }
}