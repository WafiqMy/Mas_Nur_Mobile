package com.example.masnur.Fitur_Profil_Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton; // ✅ tambahkan ini
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
    private ImageButton btnTogglePasswordBaru, btnToggleVerifikasi; // ✅ deklarasi
    private ApiService apiService;
    private String currentUsername;
    private String currentPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_kata_sandi_step2);

        // ✅ Inisialisasi semua view
        etPasswordBaru = findViewById(R.id.et_password_baru);
        etVerifikasi = findViewById(R.id.et_verifikasi);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnTogglePasswordBaru = findViewById(R.id.btnTogglePasswordBaru); // ✅
        btnToggleVerifikasi = findViewById(R.id.btnToggleVerifikasi);     // ✅
        apiService = ApiClient.getService();

        // ✅ Setup toggle visibility
        setupTogglePassword();

        Intent intent = getIntent();
        currentUsername = intent.getStringExtra("username");
        currentPass = intent.getStringExtra("current_password");

        if (currentUsername == null || currentPass == null) {
            Toast.makeText(this, "Data tidak lengkap", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSimpan.setOnClickListener(v -> {
            String passBaru = etPasswordBaru.getText().toString().trim();
            String verifikasi = etVerifikasi.getText().toString().trim();

            if (passBaru.isEmpty()) {
                Toast.makeText(this, "Password baru wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (passBaru.length() < 6) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passBaru.equals(verifikasi)) {
                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Panggil API ganti password
            Call<ReservasiResponse> call = apiService.gantiPassword(currentUsername, currentPass, passBaru);
            call.enqueue(new Callback<ReservasiResponse>() {
                @Override
                public void onResponse(Call<ReservasiResponse> call, Response<ReservasiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        if ("success".equals(status)) {
                            Toast.makeText(GantiKataSandiStep2Activity.this, message, Toast.LENGTH_SHORT).show();

                            // ✅ Arahkan ke halaman Profil Admin
                            Intent intent = new Intent(GantiKataSandiStep2Activity.this, Profil_Admin_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            finish(); // tutup activity ganti sandi
                        } else {
                            Toast.makeText(GantiKataSandiStep2Activity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(GantiKataSandiStep2Activity.this, "Respons tidak valid dari server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReservasiResponse> call, Throwable t) {
                    Toast.makeText(GantiKataSandiStep2Activity.this, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // ✅ Pisahkan logika toggle ke method terpisah (lebih rapi)
    private void setupTogglePassword() {
        btnTogglePasswordBaru.setOnClickListener(v -> {
            boolean isVisible = etPasswordBaru.getTransformationMethod() == null;
            if (isVisible) {
                etPasswordBaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
                btnTogglePasswordBaru.setImageResource(R.drawable.ic_eye_off);
            } else {
                etPasswordBaru.setTransformationMethod(null);
                btnTogglePasswordBaru.setImageResource(R.drawable.ic_eye);
            }
            etPasswordBaru.setSelection(etPasswordBaru.getText().length());
        });

        btnToggleVerifikasi.setOnClickListener(v -> {
            boolean isVisible = etVerifikasi.getTransformationMethod() == null;
            if (isVisible) {
                etVerifikasi.setTransformationMethod(PasswordTransformationMethod.getInstance());
                btnToggleVerifikasi.setImageResource(R.drawable.ic_eye_off);
            } else {
                etVerifikasi.setTransformationMethod(null);
                btnToggleVerifikasi.setImageResource(R.drawable.ic_eye);
            }
            etVerifikasi.setSelection(etVerifikasi.getText().length());
        });
    }
}