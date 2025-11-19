package com.example.masnur.Fitur_Masuk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

        btnLanjut.setOnClickListener(v -> {
            String passwordBaru = edtPasswordBaru.getText().toString().trim();
            String verifikasiPassword = edtVerifikasiPassword.getText().toString().trim();

            if (passwordBaru.isEmpty() || verifikasiPassword.isEmpty()) {
                Toast.makeText(this, "Isi semua kolom terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passwordBaru.equals(verifikasiPassword)) {
                Toast.makeText(this, "Kata sandi tidak sama, coba lagi!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Panggil API untuk reset password
            resetPassword(email, passwordBaru);
        });
    }

    private void resetPassword(String email, String passwordBaru) {
        Call<ResetResponse> call = apiService.resetPassword(email, passwordBaru);
        call.enqueue(new Callback<ResetResponse>() {
            @Override
            public void onResponse(Call<ResetResponse> call, Response<ResetResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("success".equals(response.body().getStatus())) {
                        Toast.makeText(SandiBaru.this, "Password berhasil diubah", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SandiBaru.this, MasukActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SandiBaru.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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