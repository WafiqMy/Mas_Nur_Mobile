package com.example.masnur.Fitur_Profil_Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private ImageButton btnTogglePasswordLama;
    private ApiService apiService;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_kata_sandi_step1);

        etPasswordLama = findViewById(R.id.et_password_lama);
        btnKembali = findViewById(R.id.btn_kembali);
        btnLanjutkan = findViewById(R.id.btn_lanjutkan);
        btnTogglePasswordLama = findViewById(R.id.btnTogglePasswordLama); // ✅
        apiService = ApiClient.getService();

        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        currentUsername = prefs.getString("username", "");

        if (currentUsername.isEmpty()) {
            Toast.makeText(this, "Sesi tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ✅ Toggle visibility password lama
        btnTogglePasswordLama.setOnClickListener(v -> {
            boolean isVisible = etPasswordLama.getTransformationMethod() == null;
            if (isVisible) {
                etPasswordLama.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                btnTogglePasswordLama.setImageResource(R.drawable.ic_eye_off);
            } else {
                etPasswordLama.setTransformationMethod(null);
                btnTogglePasswordLama.setImageResource(R.drawable.ic_eye);
            }
            etPasswordLama.setSelection(etPasswordLama.getText().length());
        });

        btnKembali.setOnClickListener(v -> finish());

        btnLanjutkan.setOnClickListener(v -> {
            String passLama = etPasswordLama.getText().toString().trim();
            if (passLama.isEmpty()) {
                Toast.makeText(this, "Password lama wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ VERIFIKASI PASSWORD LAMA DENGAN SERVER SEBELUM LANJUT
            Call<ReservasiResponse> call = apiService.gantiPassword(currentUsername, passLama, passLama); // dummy new_password
            call.enqueue(new Callback<ReservasiResponse>() {
                @Override
                public void onResponse(Call<ReservasiResponse> call, Response<ReservasiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        if ("success".equals(status)) {
                            // ✅ Password lama benar → lanjut ke step 2
                            Intent intent = new Intent(GantiKataSandiActivity.this, GantiKataSandiStep2Activity.class);
                            intent.putExtra("username", currentUsername);
                            intent.putExtra("current_password", passLama);
                            startActivity(intent);
                        } else {
                            // ❌ Password lama salah
                            Toast.makeText(GantiKataSandiActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(GantiKataSandiActivity.this, "Respons tidak valid dari server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReservasiResponse> call, Throwable t) {
                    Toast.makeText(GantiKataSandiActivity.this, "Gagal menghubungi server", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}