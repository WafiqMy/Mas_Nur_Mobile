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

public class GantiNamaActivity extends AppCompatActivity {

    private EditText etNamaBaru;
    private Button btnKembali, btnSimpan;
    private ApiService apiService;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_nama);

        apiService = ApiClient.getService();

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUsername = prefs.getString("username", null);
        if (currentUsername == null) {
            Toast.makeText(this, "Sesi tidak valid. Silakan login ulang.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        etNamaBaru = findViewById(R.id.et_nama_baru);
        btnKembali = findViewById(R.id.btn_kembali);
        btnSimpan = findViewById(R.id.btn_simpan);

        btnKembali.setOnClickListener(v -> finish());

        btnSimpan.setOnClickListener(v -> {
            String newName = etNamaBaru.getText().toString().trim();
            if (newName.isEmpty()) {
                Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }
            updateName(newName);
        });
    }

    private void updateName(String newName) {
        apiService.profilAction(
                currentUsername,
                "ganti_nama",
                newName,
                "",
                ""
        ).enqueue(new Callback<ReservasiResponse>() {
            @Override
            public void onResponse(Call<ReservasiResponse> call, Response<ReservasiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReservasiResponse res = response.body();
                    Toast.makeText(GantiNamaActivity.this,
                            ("success".equals(res.getStatus()) ? "✅ " : "❌ ") + res.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                    if ("success".equals(res.getStatus())) {
                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    Toast.makeText(GantiNamaActivity.this, "❌ Error server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReservasiResponse> call, Throwable t) {
                Toast.makeText(GantiNamaActivity.this, "⚠️ " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}