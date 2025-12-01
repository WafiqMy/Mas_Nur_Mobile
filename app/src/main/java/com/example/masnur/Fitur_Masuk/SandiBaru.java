package com.example.masnur.Fitur_Masuk;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.Fitur_Masuk.ResetResponse;
import com.example.masnur.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SandiBaru extends AppCompatActivity {

    private EditText edtPasswordBaru, edtVerifikasiPassword;
    private Button btnLanjut;
    private ImageButton btnTogglePassword, btnToggleConfirm;
    private String email;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandi_baru);

        email = getIntent().getStringExtra("email");
        apiService = ApiClient.getService();

        edtPasswordBaru = findViewById(R.id.edtPasswordBaru);
        edtVerifikasiPassword = findViewById(R.id.edtVerifikasiPassword);
        btnLanjut = findViewById(R.id.btnLanjut);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);
        btnToggleConfirm = findViewById(R.id.btnToggleConfirm);

        // 🔑 Toggle visibility password
        setupToggleVisibility();

        btnLanjut.setOnClickListener(v -> {
            String passwordBaru = edtPasswordBaru.getText().toString().trim();
            String verifikasiPassword = edtVerifikasiPassword.getText().toString().trim();

            if (passwordBaru.isEmpty()) {
                Toast.makeText(this, "Kata sandi baru tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (verifikasiPassword.isEmpty()) {
                Toast.makeText(this, "Verifikasi kata sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passwordBaru.equals(verifikasiPassword)) {
                Toast.makeText(this, "Kata sandi tidak sama, coba lagi!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (passwordBaru.length() < 6) {
                Toast.makeText(this, "Kata sandi minimal 6 karakter", Toast.LENGTH_SHORT).show();
                return;
            }

            resetPassword(email, passwordBaru);
        });
    }

    private void setupToggleVisibility() {
        btnTogglePassword.setOnClickListener(v -> {
            boolean isVisible = edtPasswordBaru.getTransformationMethod() == null;
            if (isVisible) {
                edtPasswordBaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
                btnTogglePassword.setImageResource(R.drawable.ic_eye_off);
            } else {
                edtPasswordBaru.setTransformationMethod(null);
                btnTogglePassword.setImageResource(R.drawable.ic_eye);
            }
            edtPasswordBaru.setSelection(edtPasswordBaru.getText().length());
        });

        btnToggleConfirm.setOnClickListener(v -> {
            boolean isVisible = edtVerifikasiPassword.getTransformationMethod() == null;
            if (isVisible) {
                edtVerifikasiPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                btnToggleConfirm.setImageResource(R.drawable.ic_eye_off);
            } else {
                edtVerifikasiPassword.setTransformationMethod(null);
                btnToggleConfirm.setImageResource(R.drawable.ic_eye);
            }
            edtVerifikasiPassword.setSelection(edtVerifikasiPassword.getText().length());
        });
    }

    private void resetPassword(String email, String passwordBaru) {
        Call<ResetResponse> call = apiService.resetPassword(email, passwordBaru);
        call.enqueue(new Callback<ResetResponse>() {
            @Override
            public void onResponse(Call<ResetResponse> call, Response<ResetResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();

                    if ("success".equals(status)) {
                        Toast.makeText(SandiBaru.this, "Password berhasil diubah", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SandiBaru.this, MasukActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SandiBaru.this, message, Toast.LENGTH_SHORT).show(); // ✅ Sudah diperbaiki
                    }
                } else {
                    Toast.makeText(SandiBaru.this, "Respons tidak valid dari server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResetResponse> call, Throwable t) {
                Toast.makeText(SandiBaru.this, "Gagal mengubah password: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}