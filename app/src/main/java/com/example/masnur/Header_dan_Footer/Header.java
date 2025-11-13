package com.example.masnur.Header_dan_Footer;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.example.masnur.R;
import com.example.masnur.Fitur_Profil_Admin.Profil_Admin_Activity;

public class Header {

    public static void setupHeader(Activity activity) {
        // Ambil view dari layout yang sudah di-include
        View headerView = activity.findViewById(R.id.headerLayout); // perhatikan huruf kecil

        ImageView iconProfil = headerView.findViewById(R.id.iconProfil);
        ImageView iconNotifikasi = headerView.findViewById(R.id.iconNotifikasi);

        iconProfil.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Profil_Admin_Activity.class);
            activity.startActivity(intent);
        });

        iconNotifikasi.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Notifikasi_Activity.class);
            activity.startActivity(intent);
        });
    }
}