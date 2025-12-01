package com.example.masnur.Fitur_Profil_Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.Fitur_Halaman_Utama.Halaman_Utama_Activity;
import com.example.masnur.Fitur_Masuk.MasukActivity;
import com.example.masnur.Header_dan_Footer.Footer;
import com.example.masnur.R;
import com.example.masnur.Fitur_Profil_Admin.UserProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profil_Admin_Activity extends AppCompatActivity {

    private Button btnGantiNama;
    private Button btnGantiSandi;
    private Button btnKeluar;
    private TextView tvUserName; // untuk menampilkan nama dari DB
    private ApiService apiService;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_admin);

        Footer.setupFooter(this);

        tvUserName = findViewById(R.id.user_name);
        btnGantiNama = findViewById(R.id.btn_ganti_nama);
        btnGantiSandi = findViewById(R.id.btn_ganti_sandi);
        btnKeluar = findViewById(R.id.btn_keluar);

        apiService = ApiClient.getService();

        // Ambil username dari SharedPreferences (simpan saat login)
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        currentUsername = prefs.getString("username", "");

        if (currentUsername.isEmpty()) {
            Toast.makeText(this, "Sesi tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadUserProfile(); // muat nama dari server

        btnGantiNama.setOnClickListener(v -> {
            Intent intent = new Intent(Profil_Admin_Activity.this, GantiNamaActivity.class);
            startActivityForResult(intent, 101);
        });

        btnGantiSandi.setOnClickListener(v -> {
            Intent intent = new Intent(Profil_Admin_Activity.this, GantiKataSandiActivity.class);
            startActivity(intent);
        });

        btnKeluar.setOnClickListener(v -> {
            // Hapus session
            prefs.edit().clear().apply();
            // Kembali ke login
            Intent intent = new Intent(Profil_Admin_Activity.this, MasukActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserProfile() {
        Call<UserProfileResponse> call = apiService.getUserProfile(currentUsername);
        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String status = response.body().getStatus();
                    if ("success".equals(status)) {
                        String nama = response.body().getData().getNama();
                        tvUserName.setText(nama); // ✅ Tampilkan nama dari kolom 'nama'
                    } else {
                        Toast.makeText(Profil_Admin_Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Profil_Admin_Activity.this, "Gagal memuat profil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(Profil_Admin_Activity.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            loadUserProfile(); // muat ulang nama setelah ganti nama
        }
    }
}