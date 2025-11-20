package com.example.masnur.Fitur_Profil_Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.Fitur_Persewaan.ReservasiResponse;
import com.example.masnur.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GantiKataSandiActivity extends AppCompatActivity {

    private EditText etPasswordLama;
    private Button btnKembali, btnLanjutkan;
    private ApiService apiService;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_kata_sandi_step1);

        apiService = ApiClient.getService();

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUsername = prefs.getString("username", null);
        if (currentUsername == null) {
            Toast.makeText(this, "Sesi tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etPasswordLama = findViewById(R.id.et_password_lama);
        btnKembali = findViewById(R.id.btn_kembali);
        btnLanjutkan = findViewById(R.id.btn_lanjutkan);

        btnKembali.setOnClickListener(v -> finish());

        btnLanjutkan.setOnClickListener(v -> {
            String currentPass = etPasswordLama.getText().toString().trim();
            if (currentPass.isEmpty()) {
                Toast.makeText(this, "Password lama tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            apiService.profilAction(
                    currentUsername,
                    "ganti_password",
                    "",
                    currentPass,
                    "temp"
            ).enqueue(new Callback<ReservasiResponse>() {
                @Override
                public void onResponse(Call<ReservasiResponse> call, Response<ReservasiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ReservasiResponse res = response.body();
                        if ("success".equals(res.getStatus())) {
                            Intent intent = new Intent(GantiKataSandiActivity.this, GantiKataSandiStep2Activity.class);
                            intent.putExtra("current_pass", currentPass);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(GantiKataSandiActivity.this, "❌ " + res.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(GantiKataSandiActivity.this, "❌ Gagal validasi", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReservasiResponse> call, Throwable t) {
                    Toast.makeText(GantiKataSandiActivity.this, "⚠️ " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}