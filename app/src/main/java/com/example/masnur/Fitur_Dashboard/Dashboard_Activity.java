package com.example.masnur.Fitur_Dashboard;

public class Dashboard_Activity {

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

    public class ProfilActivity extends AppCompatActivity {

        // Deklarasi komponen
        private TextView userNameText, userRoleText;
        private ImageView masjidLogo;
        private Button btnGantiNama, btnGantiSandi, btnKeluar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profil);

            // Inisialisasi komponen dari XML
            userNameText = findViewById(R.id.user_name);
            userRoleText = findViewById(R.id.user_role);
            masjidLogo = findViewById(R.id.masjid_logo);
            btnGantiNama = findViewById(R.id.btn_ganti_nama);
            btnGantiSandi = findViewById(R.id.btn_ganti_sandi);
            btnKeluar = findViewById(R.id.btn_keluar);

            // Contoh: Set data user (bisa diambil dari SharedPreferences / API nanti)
            userNameText.setText("M. Wafiq Marzuq Yuwono");
            userRoleText.setText("Marbot Masjid");

            // --- EVENT HANDLER ---

            btnGantiNama.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Contoh aksi: pindah ke halaman ubah nama
                    Toast.makeText(ProfilActivity.this, "Menu Ganti Nama ditekan", Toast.LENGTH_SHORT).show();

                    // Contoh jika kamu punya activity lain:
                    // startActivity(new Intent(ProfilActivity.this, GantiNamaActivity.class));
                }
            });

            btnGantiSandi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ProfilActivity.this, "Menu Ganti Kata Sandi ditekan", Toast.LENGTH_SHORT).show();
                }
            });

            btnKeluar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ProfilActivity.this, "Anda telah keluar", Toast.LENGTH_SHORT).show();

                    // Contoh logika keluar: kembali ke halaman login
                    // startActivity(new Intent(ProfilActivity.this, LoginActivity.class));
                    // finish();
                }
            });
        }
    }

}
