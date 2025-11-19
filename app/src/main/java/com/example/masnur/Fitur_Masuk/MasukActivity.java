package com.example.masnur.Fitur_Masuk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masnur.Api.ApiClient;
import com.example.masnur.Api.ApiService;
import com.example.masnur.Fitur_Halaman_Utama.Halaman_Utama_Activity;
import com.example.masnur.Fitur_Masuk.LoginResponse;
import com.example.masnur.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasukActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnMasuk;
    TextView tvLupaPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnMasuk = findViewById(R.id.btnMasuk);
        tvLupaPassword = findViewById(R.id.tvLupaSandi);

        btnMasuk.setOnClickListener(view -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            prosesLogin(username, password);
        });

        tvLupaPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MasukActivity.this, KonfirmasiEmail.class);
            startActivity(intent);
        });
    }

    private void prosesLogin(String username, String password) {
        ApiService apiService = ApiClient.getService();
        Call<LoginResponse> call = apiService.loginAdmin(username, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String status = response.body().getStatus();
                    if (status.equals("success")) {
                        Toast.makeText(MasukActivity.this, "Login berhasil sebagai admin", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MasukActivity.this, Halaman_Utama_Activity.class));
                        finish();
                    } else {
                        Toast.makeText(MasukActivity.this, "Login gagal. Hanya admin yang bisa masuk.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MasukActivity.this, "Respons tidak valid dari server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginError", "Retrofit error: " + t.getMessage());
                Toast.makeText(MasukActivity.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}