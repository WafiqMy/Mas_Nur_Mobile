package com.example.masnur.profilmasjid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivityProfilMasjid extends AppCompatActivity {

    private Button btnSejarah, btnStruktur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_masjid);

        btnSejarah = findViewById(R.id.btnSejarah);
        btnStruktur = findViewById(R.id.btnStruktur);

        // Set ripple effect (efek klik halus)
        applyButtonEffect(btnSejarah);
        applyButtonEffect(btnStruktur);

        // Tampilkan fragment pertama (Sejarah Masjid)
        replaceFragment(new SejarahFragment(), true);
        btnSejarah.setSelected(true);
        btnStruktur.setSelected(false);

        // Tombol Sejarah Masjid
        btnSejarah.setOnClickListener(v -> {
            if (!btnSejarah.isSelected()) {
                btnSejarah.setSelected(true);
                btnStruktur.setSelected(false);
                replaceFragment(new SejarahFragment(), true);
            }
        });

        // Tombol Struktur Organisasi
        btnStruktur.setOnClickListener(v -> {
            if (!btnStruktur.isSelected()) {
                btnStruktur.setSelected(true);
                btnSejarah.setSelected(false);
                replaceFragment(new StrukturFragment(), false);
            }
        });
    }

    /**
     * Fungsi mengganti fragment dengan animasi lembut (slide + fade)
     */
    private void replaceFragment(Fragment fragment, boolean fromLeft) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (fromLeft) {
            // Slide dari kiri ke kanan (Sejarah)
            transaction.setCustomAnimations(
                    R.anim.slide_in_left,    // fragment baru masuk
                    R.anim.slide_out_right,  // fragment lama keluar
                    R.anim.slide_in_right,   // saat kembali
                    R.anim.slide_out_left    // saat keluar kembali
            );
        } else {
            // Slide dari kanan ke kiri (Struktur)
            transaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );
        }

        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Tambahkan efek ripple (klik halus) ke tombol
     */
    private void applyButtonEffect(View view) {
        // Gunakan ripple dari tema Material Design
        view.setBackgroundResource(android.R.drawable.btn_default);
        view.setClickable(true);
        view.setFocusable(true);
    }
}
