package com.example.masnur.Header_dan_Footer;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.example.masnur.R;
import com.example.masnur.Fitur_Halaman_Utama.Halaman_Utama_Activity;
import com.example.masnur.Fitur_Event.Event_Activity;
import com.example.masnur.Fitur_Informasi_Masjid.Informasi_Masjid_Activity;
import com.example.masnur.Fitur_Reservasi.Reservasi_Activity;
import com.example.masnur.Fitur_Berita.Berita_Activity;

public class Footer {

    public static void setupFooter(Activity activity) {
        // Ambil view dari layout yang sudah di-include
        View footerView = activity.findViewById(R.id.footerLayout);

        ImageView iconBeranda = footerView.findViewById(R.id.iconBeranda);
        ImageView iconEvent = footerView.findViewById(R.id.iconEvent);
        ImageView iconInformasi = footerView.findViewById(R.id.iconInformasi);
        ImageView iconReservasi = footerView.findViewById(R.id.iconReservasi);
        ImageView iconBerita = footerView.findViewById(R.id.iconBerita);

        iconBeranda.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Halaman_Utama_Activity.class);
            activity.startActivity(intent);
        });

        iconEvent.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Event_Activity.class);
            activity.startActivity(intent);
        });

        iconInformasi.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Informasi_Masjid_Activity.class);
            activity.startActivity(intent);
        });

        iconReservasi.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Reservasi_Activity.class);
            activity.startActivity(intent);
        });

        iconBerita.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Berita_Activity.class);
            activity.startActivity(intent);
        });
    }
}