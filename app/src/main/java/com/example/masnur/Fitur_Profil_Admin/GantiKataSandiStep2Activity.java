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

public class GantiKataSandiStep2Activity extends AppCompatActivity {

    private EditText etPasswordBaru, etVerifikasi;
    private Button btnSimpan;
    private ApiService apiService;
    private String currentUsername;
    private String currentPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_kata_sandi_step2);

        apiService = ApiClient.getService();

        currentPass = getIntent().getStringExtra("current_pass");
        if (currentPass == null || currentPass.isEmpty()) {
            Toast.makeText(this, "Sesi tidak valid. Silakan ulangi.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUsername = prefs.getString("username", null);
        if (currentUsername == null) {
            Toast.makeText(this, "Sesi tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etPasswordBaru = findViewById(R.id.et_password_baru);
        etVerifikasi = findViewById(R.id.et_verifikasi);
        btnSimpan = findViewById(R.id.btn_simpan);

        btnSimpan.setOnClickListener(v -> {
            String newPassword = etPasswordBaru.getText().toString().trim();
            String verify = etVerifikasi.getText().toString().trim();

            if (newPassword.isEmpty() || verify.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPassword.equals(verify)) {
                Toast.makeText(this, "❌ Password baru dan verifikasi tidak cocok", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newPassword.length() < 6) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
                return;
            }

            apiService.profilAction(
                    currentUsername,
                    "ganti_password",
                    "",
                    currentPass,
                    newPassword
            ).enqueue(new Callback<ReservasiResponse>() {
                @Override
                public void onResponse(Call<ReservasiResponse> call, Response<ReservasiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ReservasiResponse res = response.body();
                        Toast.makeText(GantiKataSandiStep2Activity.this,
                                ("success".equals(res.getStatus()) ? "✅ " : "❌ ") + res.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                        if ("success".equals(res.getStatus())) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    } else {
                        Toast.makeText(GantiKataSandiStep2Activity.this, "❌ Gagal simpan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReservasiResponse> call, Throwable t) {
                    Toast.makeText(GantiKataSandiStep2Activity.this, "⚠️ " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}