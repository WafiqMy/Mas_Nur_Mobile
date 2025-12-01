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

        etNamaBaru = findViewById(R.id.et_nama_baru);
        btnKembali = findViewById(R.id.btn_kembali);
        btnSimpan = findViewById(R.id.btn_simpan);
        apiService = ApiClient.getService();

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        currentUsername = prefs.getString("username", "");

        if (currentUsername.isEmpty()) {
            Toast.makeText(this, "Sesi tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnKembali.setOnClickListener(v -> finish());

        btnSimpan.setOnClickListener(v -> {
            String namaBaru = etNamaBaru.getText().toString().trim();
            if (namaBaru.isEmpty()) {
                Toast.makeText(this, "Nama baru tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Panggil API ganti nama
            Call<ReservasiResponse> call = apiService.gantiNama(currentUsername, namaBaru);
            call.enqueue(new Callback<ReservasiResponse>() {
                @Override
                public void onResponse(Call<ReservasiResponse> call, Response<ReservasiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        if ("success".equals(status)) {
                            Toast.makeText(GantiNamaActivity.this, message, Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK); // trigger reload di Profil_Admin_Activity
                            finish();
                        } else {
                            Toast.makeText(GantiNamaActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(GantiNamaActivity.this, "Respons tidak valid dari server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReservasiResponse> call, Throwable t) {
                    Toast.makeText(GantiNamaActivity.this, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}